package com.jfx4test.framework.robot;

import java.util.Objects;

import com.jfx4test.framework.robot.MouseRobot;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.VerticalDirection;


public class ScrollRobot {


    private static final int SCROLL_ONE_UP_OR_LEFT = -1;
    private static final int SCROLL_ONE_DOWN_OR_RIGHT = 1;

    private final com.jfx4test.framework.robot.MouseRobot mouseRobot;

    public ScrollRobot(MouseRobot mouseRobot) {
        Objects.requireNonNull(mouseRobot, "mouseRobot must not be null");
        this.mouseRobot = mouseRobot;
    }


    public void scroll(int amount) {
        if (amount >= 0) {
            scrollDown(amount);
        }
        else {
            scrollUp(Math.abs(amount));
        }
    }


    public void scroll(int positiveAmount, VerticalDirection direction) {
        switch (direction) {
            case UP:
                scrollUp(positiveAmount);
                break;
            case DOWN:
                scrollDown(positiveAmount);
                break;
            default:
                throw new IllegalArgumentException("unknown vertical direction: " + direction);
        }
    }


    public void scrollUp(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_UP_OR_LEFT);
        }
    }


    public void scrollDown(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_DOWN_OR_RIGHT);
        }
    }


    public void scroll(int positiveAmount, HorizontalDirection direction) {
        switch (direction) {
            case RIGHT:
                scrollRight(positiveAmount);
                break;
            case LEFT:
                scrollLeft(positiveAmount);
                break;
            default:
                throw new IllegalArgumentException("unknown horizontal direction: " + direction);
        }
    }


    public void scrollRight(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_DOWN_OR_RIGHT);
        }
    }


    public void scrollLeft(int positiveAmount) {
        for (int scrollTick = 0; scrollTick < positiveAmount; scrollTick++) {
            mouseRobot.scroll(SCROLL_ONE_UP_OR_LEFT);
        }
    }
}
