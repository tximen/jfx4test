package com.jfx4test.framework.util;

import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class ApplicationFixture {

    protected final Object testInstance;
    private final List<Method> init;
    private final List<Method> stop;

    public ApplicationFixture(Object testInstance, List<Method> init, List<Method> stop) {
        this.testInstance = testInstance;
        this.init = init;
        this.stop = stop;
    }

    public final void init() {
       this.init.forEach(this::call);
    }

    public abstract void start(Stage stage) throws Exception;

    public final void stop()  {

        this.stop.forEach(this::call);
    }

    private void call(Method method) {
        try {
            method.invoke(this.testInstance);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new IllegalStateException("method call %s failed".formatted(method.getName()), exception);
        }
    }

}

