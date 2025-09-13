package com.jfx4test.framework.junit;

import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.api.FxToolkit;

import com.jfx4test.framework.fxml.FxmlMethodControllerFactory;
import com.jfx4test.framework.util.ApplicationAdapter;
import com.jfx4test.framework.util.ApplicationFixture;
import com.jfx4test.framework.util.WaitForAsyncUtils;
import javafx.util.Callback;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ApplicationExtension  extends FxRobot implements BeforeEachCallback, AfterEachCallback,
        TestInstancePostProcessor, ParameterResolver {

    private static final Logger LOGGER = Logger.getLogger("com.jfx4test.framework.junit.ApplicationExtension");

    private ApplicationFixture applicationFixture;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        this.applicationFixture = createApplicationFixture(testInstance);
        copyFields(testInstance);
    }

    private ApplicationFixture createApplicationFixture(Object testInstance) {
        Optional<FxmlSource> fxmlSource = findFxmlSource(testInstance);
        if (fxmlSource.isPresent()) {
            return createApplicationFxmlFixture(testInstance, fxmlSource.get());
        } else {
           return createApplicationPlainFixture(testInstance);
        }

    }

    private ApplicationPlainFixture createApplicationPlainFixture(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        return new ApplicationPlainFixture(
               testInstance,
               findMethods(testClass, this::validateInitMethod),
               findMethods(testClass, this::validateStartMethod),
               findMethods(testClass, this::validateStopMethod));
    }

    private ApplicationFxmlFixture createApplicationFxmlFixture(Object testInstance, FxmlSource fxmlSource) {
        Class<?> testClass = testInstance.getClass();
        return new ApplicationFxmlFixture(
                testInstance,
                fxmlSource,
                createControllerFactory(testClass, testInstance),
                findMethods(testClass, this::validateInitMethod),
                findMethods(testClass, this::validateStopMethod)
        );
    }


    private boolean validateInitMethod(Method initMethod) {
        if (initMethod.isAnnotationPresent(Init.class)) {
            if (initMethod.getParameterCount() != 0) {
                throw new IllegalStateException("Method annotated with @Init should have no arguments");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validateStartMethod(Method startMethod) {
        if (startMethod.isAnnotationPresent(Start.class)) {
            Class<?>[] parameterTypes = startMethod.getParameterTypes();
            if (parameterTypes.length != 1 || !parameterTypes[0].isAssignableFrom(javafx.stage.Stage.class)) {
                throw new IllegalStateException("Method annotated with @Start should have one argument of type " +
                        "javafx.stage.Stage");
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean validateStopMethod(Method stopMethod) {
        if (stopMethod.isAnnotationPresent(Stop.class)) {
            if (stopMethod.getParameterCount() != 0) {
                throw new IllegalStateException("Method annotated with @Stop should have no arguments");
            }
            return true;
        } else {
            return false;
        }

    }

    private Optional<FxmlSource> findFxmlSource(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        if (testClass.isAnnotationPresent(FxmlSource.class)) {
            FxmlSource source = testClass.getAnnotation(FxmlSource.class);
            LOGGER.fine("fxml source %s".formatted(source.value()));
            return Optional.of(source);
        } else {
            return Optional.empty();
        }
    }


    private Callback<Class<?>, Object> createControllerFactory(Class<?> testClass, Object testInstance) {
        Optional<Method> factoryMethod = findControllerFactory(testClass);
        if (factoryMethod.isPresent()) {
            Method caller = factoryMethod.get();
            LOGGER.info("use controller %s".formatted(caller.getName()));
            return new FxmlMethodControllerFactory(testInstance, caller);
        } else {
            LOGGER.info("use default controller factory");
            return this::createInstance;
        }
    }


    private Optional<Method> findControllerFactory(Class<?> testClass) {
        return Arrays.stream(testClass.getDeclaredMethods()).filter(this::controllerFactory).findFirst();
    }


    private boolean controllerFactory(Method method) {
        return method.isAnnotationPresent(FxmlControllerFactory.class)
                && factoryMethodValid(method);

    }


    private boolean factoryMethodValid(Method method) {
        if (Object.class.equals(method.getReturnType())
                && method.getParameterCount() == 1
                && Class.class.equals(method.getParameters()[0].getType())) {
            return true;
        } else {
            LOGGER.fine("""
                    invalid controller factory [%s %s(..)]
                    -- proposal --
                       Object %s(Class<?> reference) {
                          ...
                       }""".formatted(method.getReturnType().getSimpleName(), method.getName(), method.getName()));
        }

        return false;
    }

    public Object createInstance(Class<?> refClass) {
        try {
            LOGGER.info("create instance of %s".formatted(refClass.getName()));
            return refClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException exception) {
            LOGGER.log(Level.SEVERE, exception, () -> "cannot create an instance of %s".formatted(refClass.getName()));
            throw new IllegalStateException("cannot create an instance of %s".formatted(refClass.getName()), exception);
        }
    }


    private List<Method> findMethods(Class<?> testClass, Predicate<Method> predicate) {
        List<Method> methodList =
                Arrays.stream(testClass.getDeclaredMethods())
                        .filter(predicate)
                        .collect(Collectors.toList());
        methodList.forEach(method -> method.setAccessible(true));
        return methodList;
    }


    private void copyFields(Object testInstance) throws IllegalAccessException {
        Class<?> testClass = testInstance.getClass();
        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(FxRobot.class)) {
                setField(testInstance, field, this);
            }
        }
    }

    private void setField(Object instance, Field field, Object value) throws IllegalAccessException {
        boolean wasAccessible = field.canAccess(instance);
        try {
            field.setAccessible(true);
            field.set(instance, value);
        }
        finally {
            field.setAccessible(wasAccessible);
        }
    }
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        FxToolkit.cleanupAfterTest(this, new ApplicationAdapter(this.applicationFixture));
        // Required to wait for the end of the UI events processing
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(this.applicationFixture));
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(FxRobot.class);
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return this;
    }


}
