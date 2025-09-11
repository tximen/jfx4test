package com.jfx4test.framework.robot;
import java.util.Objects;

import com.jfx4test.framework.robot.Motion;
import com.jfx4test.framework.robot.MouseRobot;
import com.jfx4test.framework.robot.MoveRobot;
import com.jfx4test.framework.robot.SleepRobot;
import javafx.scene.input.MouseButton;


import com.jfx4test.framework.service.query.PointQuery;
public class ClickRobot {

    private static final long SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS = 50;

    private final MouseRobot mouseRobot;
    private final com.jfx4test.framework.robot.MoveRobot moveRobot;
    private final SleepRobot sleepRobot;

    public ClickRobot(MouseRobot mouseRobot, MoveRobot moveRobot, SleepRobot sleepRobot) {
        Objects.requireNonNull(mouseRobot, "mouseRobot must not be null");
        Objects.requireNonNull(moveRobot, "moveRobot must not be null");
        Objects.requireNonNull(sleepRobot, "sleepRobot must not be null");
        this.mouseRobot = mouseRobot;
        this.moveRobot = moveRobot;
        this.sleepRobot = sleepRobot;
    }


    public void clickOn(MouseButton... buttons) {
        mouseRobot.pressNoWait(buttons);
        mouseRobot.release(buttons);
    }


    public void clickOn(PointQuery pointQuery, com.jfx4test.framework.robot.Motion motion, MouseButton... buttons) {
        moveRobot.moveTo(pointQuery, motion);
        clickOn(buttons);
    }


    public void doubleClickOn(MouseButton... buttons) {
        clickOn(buttons);
        clickOn(buttons);
        sleepRobot.sleep(SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS);
    }


    public void doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        moveRobot.moveTo(pointQuery, motion);
        clickOn(buttons);
        clickOn(buttons);
        sleepRobot.sleep(SLEEP_AFTER_DOUBLE_CLICK_IN_MILLIS);
    }

}
