package com.jfx4test.framework.service.adapter;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;

import static com.jfx4test.framework.util.WaitForAsyncUtils.asyncFx;
import static com.jfx4test.framework.util.WaitForAsyncUtils.runOnFxThread;
import static com.jfx4test.framework.util.WaitForAsyncUtils.waitForAsyncFx;

/**
 * since we use javafx 25 the Robot class is available
 */
public class GlassRobotAdapter implements RobotAdapter<Robot> {

    private static final int RETRIEVAL_TIMEOUT_IN_MILLIS = 10_000;
    private Robot glassRobot;

    private void validateRobot() {
        if (this.glassRobot == null) {
            runOnFxThread(this::robotCreate);
        }
    }

    @Override
    public void robotCreate() {
        this.glassRobot = new Robot();
    }

    @Override
    public void robotDestroy() {
        this.glassRobot = null;
    }

    @Override
    public void keyPress(KeyCode key) {
        this.glassRobot.keyPress(key);
    }

    @Override
    public void keyRelease(KeyCode key) {
        this.glassRobot.keyRelease(key);
    }

    @Override
    public Point2D getMouseLocation() {
        validateRobot();
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS, this::createPoint);
    }

    private Point2D createPoint() {
        validateRobot();
        return  new Point2D(this.glassRobot.getMouseX(), this.glassRobot.getMouseY());
    }

    @Override
    public void mouseMove(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        asyncFx(() -> this.glassRobot.mouseMove(scaled.getMinX(),  scaled.getMinY()));
    }

    @Override
    public void mousePress(MouseButton button) {
        runOnFxThread(() ->  this.glassRobot.mousePress(button));
    }

    @Override
    public void mouseRelease(MouseButton button) {
        runOnFxThread(() -> this.glassRobot.mouseRelease(button));
    }

    @Override
    public void mouseWheel(int wheelAmount) {

    }

    @Override
    public Color getCapturePixelColor(Point2D location) {
        final Rectangle2D scaled = new Rectangle2D(location.getX(), location.getY(), 0, 0);
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
                () ->  this.glassRobot.getPixelColor(scaled.getMinX(),  scaled.getMinY()));
    }

    @Override
    public Image getCaptureRegion(Rectangle2D region) {
        return waitForAsyncFx(RETRIEVAL_TIMEOUT_IN_MILLIS,
                () -> this.glassRobot.getScreenCapture(null,  region.getMinX(), region.getMinY(), region.getWidth(), region.getHeight(), false));
    }
}
