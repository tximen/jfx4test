package com.jfx4test.framework.api;

import com.jfx4test.framework.api.FxApiFxApiContextHolder;
import com.jfx4test.framework.api.FxRobot;
import javafx.geometry.Pos;

import com.jfx4test.framework.robot.BaseRobot;
import com.jfx4test.framework.robot.KeyboardRobot;
import com.jfx4test.framework.robot.MouseRobot;
import com.jfx4test.framework.robot.MoveRobot;
import com.jfx4test.framework.robot.SleepRobot;
import com.jfx4test.framework.robot.ClickRobot;
import com.jfx4test.framework.robot.DragRobot;
import com.jfx4test.framework.robot.ScrollRobot;
import com.jfx4test.framework.robot.TypeRobot;
import com.jfx4test.framework.robot.WriteRobot;
import com.jfx4test.framework.service.finder.WindowFinder;
import com.jfx4test.framework.service.finder.NodeFinder;
import com.jfx4test.framework.service.locator.BoundsLocator;
import com.jfx4test.framework.service.locator.PointLocator;
import com.jfx4test.framework.service.support.CaptureSupport;

/**
 * Stores the robot implementations, the window and node finders, position calculators, and capture support for
 * {@link FxRobot}.
 */
public class FxRobotContext {

    private final NodeFinder nodeFinder;
    private final BoundsLocator boundsLocator;
    private final PointLocator pointLocator;
    private final BaseRobot baseRobot;
    private final MouseRobot mouseRobot;
    private final KeyboardRobot keyboardRobot;
    private final MoveRobot moveRobot;
    private final SleepRobot sleepRobot;
    private final ClickRobot clickRobot;
    private final DragRobot dragRobot;
    private final ScrollRobot scrollRobot;
    private final TypeRobot typeRobot;
    private final WriteRobot writeRobot;
    private final CaptureSupport captureSupport;
    private Pos pointPosition;

    public FxRobotContext() {
        nodeFinder = FxApiFxApiContextHolder.getInstance().getApiContext().nodeFinder() ;
        boundsLocator = new BoundsLocator();
        pointLocator = new PointLocator(boundsLocator);
        baseRobot = new BaseRobot();
        keyboardRobot = new KeyboardRobot(baseRobot);
        mouseRobot = new MouseRobot(baseRobot);
        sleepRobot = new SleepRobot();
        typeRobot = new TypeRobot(keyboardRobot, sleepRobot);
        writeRobot = new WriteRobot(baseRobot, sleepRobot, nodeFinder.getWindowFinder());
        moveRobot = new MoveRobot(baseRobot, mouseRobot, sleepRobot);
        clickRobot = new ClickRobot(mouseRobot, moveRobot, sleepRobot);
        dragRobot = new DragRobot(mouseRobot, moveRobot);
        scrollRobot = new ScrollRobot(mouseRobot);
        captureSupport = new CaptureSupport(baseRobot);
        pointPosition = Pos.CENTER;
    }

    public WindowFinder getWindowFinder() {
        return this.nodeFinder.getWindowFinder();
    }

    public NodeFinder getNodeFinder() {
        return nodeFinder;
    }

    public Pos getPointPosition() {
        return pointPosition;
    }

    public void setPointPosition(Pos pointPosition) {
        this.pointPosition = pointPosition;
    }

    public BoundsLocator getBoundsLocator() {
        return boundsLocator;
    }

    public PointLocator getPointLocator() {
        return pointLocator;
    }

    public BaseRobot getBaseRobot() {
        return baseRobot;
    }

    public MouseRobot getMouseRobot() {
        return mouseRobot;
    }

    public KeyboardRobot getKeyboardRobot() {
        return keyboardRobot;
    }

    public MoveRobot getMoveRobot() {
        return moveRobot;
    }

    public SleepRobot getSleepRobot() {
        return sleepRobot;
    }

    public ClickRobot getClickRobot() {
        return clickRobot;
    }

    public DragRobot getDragRobot() {
        return dragRobot;
    }

    public ScrollRobot getScrollRobot() {
        return scrollRobot;
    }

    public TypeRobot getTypeRobot() {
        return typeRobot;
    }

    public WriteRobot getWriteRobot() {
        return writeRobot;
    }

    public CaptureSupport getCaptureSupport() {
        return captureSupport;
    }

}
