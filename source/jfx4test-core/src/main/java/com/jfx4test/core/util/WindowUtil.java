package com.jfx4test.framework.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Window;

public class WindowUtil {

    /*
    public static List<Window> getWindows() {
        return new ArrayList<>(Window.getWindows());
    }
*/

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
