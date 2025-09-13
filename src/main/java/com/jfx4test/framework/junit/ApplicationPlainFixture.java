package com.jfx4test.framework.junit;

import com.jfx4test.framework.util.ApplicationFixture;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ApplicationPlainFixture extends ApplicationFixture {

    private final List<Method> start;

    public ApplicationPlainFixture(Object testInstance, List<Method> init,
                                    List<Method> start, List<Method> stop) {
        super(testInstance, init, stop);
        this.start = start;
    }

    @Override
    public void start(Stage stage) throws InvocationTargetException, IllegalAccessException {
        for (Method method : start) {
            method.invoke(testInstance, stage);
        }
    }

}
