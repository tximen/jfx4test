package com.jfx4test.framework.fxml;

import javafx.util.Callback;

import java.lang.reflect.Field;

public class FxmlFieldControllerFactory implements Callback<Class<?>, Object> {

    private final Object instance;
    private final Field controllerField;

    public FxmlFieldControllerFactory(Object instance, Field controllerField) {
        this.instance = instance;
        this.controllerField = controllerField;
        this.controllerField.setAccessible(true);
    }

    @Override
    public Object call(Class<?> param) {
        try {
            return new ValueValidator(controllerField.get(this.instance), param).validated();
        } catch (IllegalArgumentException | IllegalAccessException  exception) {
            String message = "field extraction failed [%s.%s]".formatted(this.instance.getClass().getSimpleName(), controllerField.getName());
            if (exception.getCause() == null) {
                throw new FxmlFactoryException(message, exception);
            } else {
                throw new FxmlFactoryException(message, exception.getCause());
            }
        }
    }
}
