package com.jfx4test.framework.junit6;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.api.FxToolkit;
import com.jfx4test.framework.util.WaitForAsyncUtils;

public class JavaFxApplicationExtension  extends FxRobot implements BeforeEachCallback, AfterEachCallback,
        TestInstancePostProcessor, ParameterResolver {

    private ApplicationFixture applicationFixture;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        List<Method> init = new ArrayList<>();
        List<Method> start = new ArrayList<>();
        List<Method> stop = new ArrayList<>();
        Class<?> testClass = testInstance.getClass();
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(Init.class)) {
                init.add(validateInitMethod(method));
            }
            if (method.isAnnotationPresent(Start.class)) {
                start.add(validateStartMethod(method));
            }
            if (method.isAnnotationPresent(Stop.class)) {
                stop.add(validateStopMethod(method));
            }
        }
        Field[] fields = testClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(FxRobot.class)) {
                setField(testInstance, field, this);
            }
        }
        applicationFixture = new AnnotationBasedApplicationFixture(testInstance, init, start, stop);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().isAssignableFrom(FxRobot.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return this;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new ApplicationAdapter(applicationFixture));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        FxToolkit.cleanupAfterTest(this, new ApplicationAdapter(applicationFixture));
        // Required to wait for the end of the UI events processing
        WaitForAsyncUtils.waitForFxEvents();
    }

    private Method validateInitMethod(Method initMethod) {
        if (initMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Method annotated with @Init should have no arguments");
        }
        return initMethod;
    }

    private Method validateStartMethod(Method startMethod) {
        Class<?>[] parameterTypes = startMethod.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].isAssignableFrom(javafx.stage.Stage.class)) {
            throw new IllegalStateException("Method annotated with @Start should have one argument of type " +
                    "javafx.stage.Stage");
        }
        return startMethod;
    }

    private Method validateStopMethod(Method stopMethod) {
        if (stopMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Method annotated with @Stop should have no arguments");
        }
        return stopMethod;
    }

    private void setField(Object instance, Field field, Object val) throws IllegalAccessException {
        boolean wasAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, val);
        }
        finally {
            field.setAccessible(wasAccessible);
        }
    }

    private static class AnnotationBasedApplicationFixture implements ApplicationFixture {

        private final Object testInstance;
        private final List<Method> init;
        private final List<Method> start;
        private final List<Method> stop;

        private AnnotationBasedApplicationFixture(Object testInstance, List<Method> init,
                                                  List<Method> start, List<Method> stop) {
            this.testInstance = testInstance;
            this.init = init;
            this.start = start;
            this.stop = stop;
        }

        @Override
        public void init() throws InvocationTargetException, IllegalAccessException {
            for (Method method : init) {
                method.invoke(testInstance);
            }
        }

        @Override
        public void start(Stage stage) throws InvocationTargetException, IllegalAccessException {
            for (Method method : start) {
                method.invoke(testInstance, stage);
            }
        }

        @Override
        public void stop() throws InvocationTargetException, IllegalAccessException {
            for (Method method : stop) {
                method.invoke(testInstance);
            }
        }

    }

}
