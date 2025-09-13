package com.jfx4test.framework.fxml;

import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FxmlSimpleMethodFactory  implements Callback<Class<?>, Object> {

    private final Object instance;
    private final Method callerMethod;

    public FxmlSimpleMethodFactory(Object instance, Method callerMethod) {
        this.instance = instance;
        this.callerMethod = callerMethod;
        this.callerMethod.setAccessible(true);
    }

    @Override
    public Object call(Class<?> param) {
        try {
            return new ValueValidator(callerMethod.invoke(this.instance), param).validated();
        } catch (IllegalAccessException | InvocationTargetException exception) {
            String message = "method call failed [%s.%s(%s.class)]".formatted(this.instance.getClass().getSimpleName(), callerMethod.getName(), param.getName());
            if (exception.getCause() == null) {
                throw new FxmlFactoryException(message, exception);
            } else {
                throw new FxmlFactoryException(message, exception.getCause());
            }
        }
    }


}

