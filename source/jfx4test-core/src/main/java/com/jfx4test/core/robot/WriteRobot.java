package com.jfx4test.framework.robot;

import java.util.Objects;
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

import com.jfx4test.framework.service.finder.WindowFinder;
import com.jfx4test.framework.util.WaitForAsyncUtils;

public class WriteRobot {

    private static final int SLEEP_AFTER_CHARACTER_IN_MILLIS;

    static {
        int writeSleep;
        try {
            writeSleep = Integer.getInteger("testfx.robot.write_sleep", 25);
        }
        catch (NumberFormatException e) {
            System.err.println("\"testfx.robot.write_sleep\" property must be a number but was: \"" +
                    System.getProperty("testfx.robot.write_sleep") + "\".\nUsing default of \"25\" milliseconds.");
            e.printStackTrace();
            writeSleep = 25;
        }
        SLEEP_AFTER_CHARACTER_IN_MILLIS = writeSleep;
    }

    private final BaseRobot baseRobot;
    private final SleepRobot sleepRobot;
    private final WindowFinder windowFinder;

    public WriteRobot(BaseRobot baseRobot, SleepRobot sleepRobot, WindowFinder windowFinder) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        Objects.requireNonNull(sleepRobot, "sleepRobot must not be null");
        Objects.requireNonNull(windowFinder, "windowFinder must not be null");
        this.baseRobot = baseRobot;
        this.sleepRobot = sleepRobot;
        this.windowFinder = windowFinder;
    }


    public void write(char character) {
        Scene scene = fetchTargetWindow().getScene();
        typeCharacterInScene(character, scene);
    }


    public void write(String text) {
        write(text, SLEEP_AFTER_CHARACTER_IN_MILLIS);
    }


    public void write(String text, int sleepMillis) {
        Scene scene = fetchTargetWindow().getScene();
        for (char character : text.chars().mapToObj(i -> (char) i).collect(Collectors.toList())) {
            typeCharacterInScene(character, scene);
            sleepRobot.sleep(sleepMillis);
        }
    }

    private Window fetchTargetWindow() {
        Window targetWindow = windowFinder.window(Window::isFocused);
        if (targetWindow == null) {
            targetWindow = windowFinder.targetWindow();
        }
        if (targetWindow == null) {
            targetWindow = windowFinder.window(0);
        }
        return targetWindow;
    }

    private void typeCharacterInScene(char character,
                                      Scene scene) {
        KeyCode key = determineKeyCode(character);
        baseRobot.typeKeyboard(scene, key, Character.toString(character));
        WaitForAsyncUtils.waitForFxEvents();
    }

    private KeyCode determineKeyCode(char character) {
        KeyCode key = KeyCode.UNDEFINED;
        key = (character == '\n') ? KeyCode.ENTER : key;
        key = (character == '\t') ? KeyCode.TAB : key;
        return key;
    }

}
