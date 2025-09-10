package com.jfx4test.core.api;

import javafx.scene.input.KeyCode;
import javafx.stage.Screen;

public final class FxToolkit {

    private FxToolkit() {}

    /**
     *
     * @return
     */
    public static double getScreenScaleX() {
        return Screen.getPrimary().getOutputScaleX();
    }


    /**
     *
     * @return
     */
    public static double getScreenScaleY() {
        return Screen.getPrimary().getOutputScaleY();
    }



    public static int convertToKeyCodeId(KeyCode keyCode) {
        return keyCode.getCode();

    }
}
