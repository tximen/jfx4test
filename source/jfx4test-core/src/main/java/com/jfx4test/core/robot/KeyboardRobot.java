package com.jfx4test.framework.robot;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.input.KeyCode;


import com.jfx4test.framework.util.WaitForAsyncUtils;

public class KeyboardRobot {

    /**
     * This key is sent depending on the platform via the Robot to Java.
     */
    private static final KeyCode OS_SPECIFIC_SHORTCUT = System.getProperty("os.name").toLowerCase(Locale.US)
            .startsWith("mac") ? KeyCode.COMMAND : KeyCode.CONTROL;

    private final BaseRobot baseRobot;
    private final Set<KeyCode> pressedKeys = ConcurrentHashMap.newKeySet();

    public KeyboardRobot(BaseRobot baseRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        this.baseRobot = baseRobot;
    }


    public void press(KeyCode... keys) {
        Arrays.asList(keys).forEach(k -> {
            pressKey(k);
            WaitForAsyncUtils.waitForFxEvents();
        });
    }


    public void pressNoWait(KeyCode... keys) {
        Arrays.asList(keys).forEach(this::pressKey);
    }


    public void release(KeyCode... keys) {
        if (keys.length == 0) {
            pressedKeys.forEach(k -> {
                releaseKey(k);
                WaitForAsyncUtils.waitForFxEvents();
            });
        } else {
            Arrays.asList(keys).forEach(k -> {
                releaseKey(k);
                WaitForAsyncUtils.waitForFxEvents();
            });
        }
    }


    public void releaseNoWait(KeyCode... keys) {
        if (keys.length == 0) {
            pressedKeys.forEach(this::releaseKey);
        }
        else {
            Arrays.asList(keys).forEach(this::releaseKey);
        }
    }


    public final Set<KeyCode> getPressedKeys() {
        return Collections.unmodifiableSet(pressedKeys);
    }

    private void pressKey(KeyCode keyCode) {
        KeyCode realKeyCode = keyCode == KeyCode.SHORTCUT ? OS_SPECIFIC_SHORTCUT : keyCode;
        if (pressedKeys.add(realKeyCode)) {
            baseRobot.pressKeyboard(realKeyCode);
        }
    }

    private void releaseKey(KeyCode keyCode) {
        KeyCode realKeyCode = keyCode == KeyCode.SHORTCUT ? OS_SPECIFIC_SHORTCUT : keyCode;
        if (pressedKeys.remove(realKeyCode)) {
            baseRobot.releaseKeyboard(realKeyCode);
        }
    }

}
