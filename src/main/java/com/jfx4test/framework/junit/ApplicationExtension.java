package com.jfx4test.framework.junit;

import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.api.FxToolkit;

import com.jfx4test.framework.fxml.ApplicationFxmlFixture;
import com.jfx4test.framework.fxml.FxmlConfig;
import com.jfx4test.framework.fxml.FxmlFieldControllerFactory;
import com.jfx4test.framework.fxml.FxmlMethodControllerFactory;
import com.jfx4test.framework.fxml.FxmlSimpleMethodFactory;
import com.jfx4test.framework.util.ApplicationAdapter;
import com.jfx4test.framework.util.ApplicationFixture;
import com.jfx4test.framework.util.WaitForAsyncUtils;
import javafx.util.Callback;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ApplicationExtension  extends FxRobot implements BeforeEachCallback, AfterEachCallback,
        TestInstancePostProcessor, ParameterResolver {

    private static final Logger LOGGER = Logger.getLogger(ApplicationExtension.class.getName());

    private ApplicationFixture applicationFixture;
    private long delayInSeconds;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        this.applicationFixture = createApplicationFixture(testInstance);
        this.delayInSeconds = findDelayInSeconds(testInstance);
        copyFields(testInstance);
    }

    private long findDelayInSeconds(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        if (testClass.isAnnotationPresent(FxmlSource.class)) {
            return testClass.getAnnotation(FxmlSource.class).delayInSeconds();
        } else if (testClass.isAnnotationPresent(ApplicationTest.class)) {
            return testClass.getAnnotation(ApplicationTest.class).delayInSeconds();
        } else {
            return -1L;
        }
    }

    private ApplicationFixture createApplicationFixture(Object testInstance) {
        Optional<FxmlConfig> fxmlSource = findFxmlSource(testInstance);
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

    private ApplicationFxmlFixture createApplicationFxmlFixture(Object testInstance, FxmlConfig fxmlSource) {
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

    private Optional<FxmlConfig> findFxmlSource(Object testInstance) {
        Class<?> testClass = testInstance.getClass();
        if (testClass.isAnnotationPresent(FxmlSource.class)) {
            FxmlSource source = testClass.getAnnotation(FxmlSource.class);
            LOGGER.fine("fxml source %s".formatted(source.value()));
            return Optional.of(new FxmlConfig(source.value(), source.width(), source.height()));
        } else
        if (testClass.isAnnotationPresent(ApplicationTest.class)) {
            ApplicationTest source = testClass.getAnnotation(ApplicationTest.class);
            if (source.value() == null || source.value().trim().isEmpty()) {
                return Optional.empty();
            } else {
                LOGGER.fine("fxml source %s".formatted(source.value()));
                return Optional.of(new FxmlConfig(source.value(), source.width(), source.height()));
            }
        } else  {
            return Optional.empty();
        }
    }


    private Callback<Class<?>, Object> createControllerFactory(Class<?> testClass, Object testInstance) {
        Optional<Field> factoryField =
                Arrays.stream(testClass.getDeclaredFields()).filter(this::controllerField).findFirst();
        if (factoryField.isPresent()) {
            Field field = factoryField.get();
            LOGGER.info("use controller field %s.%s".formatted(testClass.getSimpleName(), field.getName()));
            return new FxmlFieldControllerFactory(testInstance, field);
        }
        Optional<Method> factoryMethod =
                Arrays.stream(testClass.getDeclaredMethods()).filter(this::controllerMethod).findFirst();
        if (factoryMethod.isPresent()) {
            Method caller = factoryMethod.get();
            LOGGER.info("use factory %s".formatted(caller.getName()));
            return new FxmlSimpleMethodFactory(testInstance, caller);
        }
        factoryMethod =
           Arrays.stream(testClass.getDeclaredMethods()).filter(this::controllerFactory).findFirst();
        if (factoryMethod.isPresent()) {
            Method caller = factoryMethod.get();
            LOGGER.info("use factory %s".formatted(caller.getName()));
            return new FxmlMethodControllerFactory(testInstance, caller);
        } else {
            LOGGER.info("use default controller factory");
            return this::createInstance;
        }
    }


    private boolean controllerField(Field field) {
        return field.isAnnotationPresent(FxmlController.class);
    }


    private boolean controllerFactory(Method method) {
        return method.isAnnotationPresent(FxmlControllerFactory.class)
            && factoryMethodValid(method);

    }

    private boolean controllerMethod(Method method) {
        return method.isAnnotationPresent(FxmlController.class);
    }

    private boolean factoryMethodValid(Method method) {
        if (Object.class.equals(method.getReturnType())
            && method.getParameterCount() == 1
            && Class.class.equals(method.getParameters()[0].getType())) {
            return true;
        } else {
            throw new IllegalStateException("""
                    invalid controller factory [%s %s(..)]
                    -- proposal --
                       Object %s(Class<?> reference) {
                          ...
                       }""".formatted(method.getReturnType().getSimpleName(), method.getName(), method.getName()));
        }
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
        if (this.delayInSeconds > 0) {
            LOGGER.info("wait for %d seconds".formatted(this.delayInSeconds));
            WaitForAsyncUtils.sleep(this.delayInSeconds, TimeUnit.SECONDS);
        }
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
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return this;
    }


}
