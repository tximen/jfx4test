package com.jfx4test.framework.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;


import com.jfx4test.framework.util.WaitForAsyncUtils;

public class MouseRobot {

    private final BaseRobot baseRobot;
    private final Set<MouseButton> pressedButtons = new HashSet<>();

    public MouseRobot(BaseRobot baseRobot) {
        Objects.requireNonNull(baseRobot, "baseRobot must not be null");
        this.baseRobot = baseRobot;
    }


    public void press(MouseButton... buttons) {
        pressNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
    }


    public void pressNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            pressButton(MouseButton.PRIMARY);
        }
        else {
            Arrays.asList(buttons).forEach(this::pressButton);
        }
    }


    public void release(MouseButton... buttons) {
        releaseNoWait(buttons);
        WaitForAsyncUtils.waitForFxEvents();
    }


    public void releaseNoWait(MouseButton... buttons) {
        if (buttons.length == 0) {
            new ArrayList<>(pressedButtons).forEach(this::releaseButton);
        }
        else {
            Arrays.asList(buttons).forEach(this::releaseButton);
        }
    }


    public void move(Point2D location) {
        moveNoWait(location);
        WaitForAsyncUtils.waitForFxEvents();
    }


    public void moveNoWait(Point2D location) {
        baseRobot.moveMouse(location);
    }


    public void scroll(int wheelAmount) {
        scrollNoWait(wheelAmount);
        WaitForAsyncUtils.waitForFxEvents();
    }


    public void scrollNoWait(int wheelAmount) {
        baseRobot.scrollMouse(wheelAmount);
    }


    public final Set<MouseButton> getPressedButtons() {
        return Collections.unmodifiableSet(pressedButtons);
    }

    private void pressButton(MouseButton button) {
        if (pressedButtons.add(button)) {
            baseRobot.pressMouse(button);
        }
    }

    private void releaseButton(MouseButton button) {
        if (pressedButtons.remove(button)) {
            baseRobot.releaseMouse(button);
        }
    }
}
