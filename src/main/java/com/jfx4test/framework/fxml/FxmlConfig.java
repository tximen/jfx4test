package com.jfx4test.framework.fxml;

public record FxmlConfig(String sourcePath, String[] stylesheet, double width, double height) {

    public boolean validWithAndHeight() {
        return this.width > 0d && this.height > 0d;
    }
}
