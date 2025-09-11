package com.jfx4test.framework.robot;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import com.jfx4test.framework.service.adapter.RobotAdapter;
import com.jfx4test.framework.service.adapter.AwtRobotAdapter;
import com.jfx4test.framework.service.adapter.GlassRobotAdapter;
import com.jfx4test.framework.service.adapter.JavafxRobotAdapter;

public class BaseRobot {


    private final RobotAdapter robotAdapter;
    private final JavafxRobotAdapter javafxRobotAdapter;

    public BaseRobot() {
        boolean verbose = Boolean.getBoolean("testfx.verbose");
        // Default to "glass" if "testfx.robot" is not explicitly set.
        String robotAdapterName = System.getProperty("testfx.robot", "glass");
        switch (robotAdapterName) {
            case "awt":
                if (verbose) {
                    System.out.println("testfx: initializing AWT robot");
                }
                robotAdapter = new AwtRobotAdapter();
                break;
            case "glass":
                if (verbose) {
                    System.out.println("testfx: initializing Glass robot");
                }
                robotAdapter = new GlassRobotAdapter();
                break;
            default:
                throw new IllegalStateException(String.format(
                        "unknown robot adapter 'testfx.robot=%s' (must be 'awt' or 'glass')", robotAdapterName));
        }
        javafxRobotAdapter = new JavafxRobotAdapter();
    }


    public void pressKeyboard(KeyCode key) {
        robotAdapter.keyPress(key);
    }


    public void releaseKeyboard(KeyCode key) {
        robotAdapter.keyRelease(key);
    }


    public void typeKeyboard(Scene scene, KeyCode key, String character) {
        // KeyEvent: "For key typed events, {@code code} is always {@code KeyCode.UNDEFINED}."
        javafxRobotAdapter.robotCreate(scene);
        javafxRobotAdapter.keyPress(key);
        javafxRobotAdapter.keyType(KeyCode.UNDEFINED, character);
        javafxRobotAdapter.keyRelease(key);
    }


    public Point2D retrieveMouse() {
        return robotAdapter.getMouseLocation();
    }


    public void moveMouse(Point2D point) {
        robotAdapter.mouseMove(point);
    }


    public void scrollMouse(int amount) {
        robotAdapter.mouseWheel(amount);
    }


    public void pressMouse(MouseButton button) {
        robotAdapter.mousePress(button);
    }


    public void releaseMouse(MouseButton button) {
        robotAdapter.mouseRelease(button);
    }


    public Image captureRegion(Rectangle2D region) {
        return robotAdapter.getCaptureRegion(region);
    }
}
