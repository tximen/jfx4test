package com.jfx4test.framework.robot;

import java.util.Objects;
import javafx.scene.input.MouseButton;

import com.jfx4test.framework.service.query.PointQuery;

public class DragRobot {


    private final MouseRobot mouseRobot;
    private final MoveRobot moveRobot;

    public DragRobot(MouseRobot mouseRobot, MoveRobot moveRobot) {
        Objects.requireNonNull(mouseRobot, "mouseRobot must not be null");
        Objects.requireNonNull(moveRobot, "moveRobot must not be null");
        this.mouseRobot = mouseRobot;
        this.moveRobot = moveRobot;
    }


    public void drag(MouseButton... buttons) {
        mouseRobot.press(buttons);
    }


    public void drag(PointQuery pointQuery, MouseButton... buttons) {
        moveRobot.moveTo(pointQuery);
        drag(buttons);
    }


    public void drop() {
        mouseRobot.release();
    }


    public void dropTo(PointQuery pointQuery) {
        moveRobot.moveTo(pointQuery);
        drop();
    }


    public void dropBy(double x, double y) {
        moveRobot.moveBy(x, y);
        drop();
    }

}
