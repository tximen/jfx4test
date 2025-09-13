package com.jfx4test.framework.fxml;

public record ValueValidator(Object value, Class<?> expectedClass) {

    public Object validated() {
        if (value == null) {
            throw new FxmlFactoryException("controller field must not be null");
        }
        if (!value.getClass().isAssignableFrom(expectedClass)) {
            throw new FxmlFactoryException("field controller class %s is not assignable from %s".formatted(value.getClass().getName(), expectedClass.getName()));
        }
        return value;
    }
}
