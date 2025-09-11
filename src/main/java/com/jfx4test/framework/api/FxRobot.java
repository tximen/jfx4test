package com.jfx4test.framework.api;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.jfx4test.framework.api.FxRobotContext;
import com.jfx4test.framework.api.FxRobotException;
import javafx.geometry.Bounds;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.stage.Window;


import com.jfx4test.framework.robot.Motion;
import com.jfx4test.framework.service.locator.PointLocator;
import com.jfx4test.framework.service.query.BoundsQuery;
import com.jfx4test.framework.service.query.NodeQuery;
import com.jfx4test.framework.service.query.PointQuery;
import com.jfx4test.framework.service.support.Capture;
import com.jfx4test.framework.util.BoundsQueryUtils;

import static com.jfx4test.framework.util.NodeQueryUtils.isVisible;
import static com.jfx4test.framework.util.WaitForAsyncUtils.asyncFx;
import static com.jfx4test.framework.util.WaitForAsyncUtils.waitFor;
import static com.jfx4test.framework.util.WaitForAsyncUtils.waitForFxEvents;


public class FxRobot {


    private final FxRobotContext context;

    /**
     * Constructs all robot-related implementations and sets {@link #targetPos(Pos)} to {@link Pos#CENTER}.
     */
    public FxRobot() {
        context = new FxRobotContext();
    }

    /**
     * Returns the internal context.
     */
    public FxRobotContext robotContext() {
        return context;
    }


    public Window targetWindow() {
        return context.getWindowFinder().targetWindow();
    }


    public FxRobot targetWindow(Window window) {
        context.getWindowFinder().targetWindow(window);
        return this;
    }


    public FxRobot targetWindow(Predicate<Window> predicate) {
        context.getWindowFinder().targetWindow(predicate);
        return this;
    }


    public FxRobot targetWindow(int windowNumber) {
        context.getWindowFinder().targetWindow(windowNumber);
        return this;
    }


    public FxRobot targetWindow(String stageTitleRegex) {
        context.getWindowFinder().targetWindow(stageTitleRegex);
        return this;
    }


    public FxRobot targetWindow(Pattern stageTitlePattern) {
        context.getWindowFinder().targetWindow(stageTitlePattern);
        return this;
    }


    public FxRobot targetWindow(Scene scene) {
        context.getWindowFinder().targetWindow(scene);
        return this;
    }


    public FxRobot targetWindow(Node node) {
        context.getWindowFinder().targetWindow(node);
        return this;
    }



    public List<Window> listTargetWindows() {
        return context.getWindowFinder().listTargetWindows();
    }


    public Window window(Predicate<Window> predicate) {
        return context.getWindowFinder().window(predicate);
    }


    public Window window(int windowIndex) {
        return context.getWindowFinder().window(windowIndex);
    }


    public Window window(String stageTitleRegex) {
        return context.getWindowFinder().window(stageTitleRegex);
    }


    public Window window(Pattern stageTitlePattern) {
        return context.getWindowFinder().window(stageTitlePattern);
    }


    public Window window(Scene scene) {
        return context.getWindowFinder().window(scene);
    }


    public Window window(Node node) {
        return context.getWindowFinder().window(node);
    }


    public NodeQuery fromAll() {
        return context.getNodeFinder().fromAll();
    }


    public NodeQuery from(Node... parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }


    public NodeQuery from(Collection<Node> parentNodes) {
        return context.getNodeFinder().from(parentNodes);
    }


    public NodeQuery from(NodeQuery nodeQuery) {
        return context.getNodeFinder().from(nodeQuery);
    }


    public NodeQuery lookup(String query) {
        return context.getNodeFinder().lookup(query);
    }


    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return context.getNodeFinder().lookup(predicate);
    }


    public Node rootNode(Window window) {
        return context.getNodeFinder().rootNode(window);
    }


    public Node rootNode(Scene scene) {
        return context.getNodeFinder().rootNode(scene);
    }


    public Node rootNode(Node node) {
        return context.getNodeFinder().rootNode(node);
    }


    public BoundsQuery bounds(double minX, double minY, double width, double height) {
        return () -> BoundsQueryUtils.bounds(minX, minY, width, height);
    }


    public BoundsQuery bounds(Point2D point) {
        return () -> BoundsQueryUtils.bounds(point);
    }


    public BoundsQuery bounds(Bounds bounds) {
        return () -> bounds;
    }


    public BoundsQuery bounds(Node node) {
        return () -> BoundsQueryUtils.boundsOnScreen(node);
    }


    public BoundsQuery bounds(Scene scene) {
        return () -> BoundsQueryUtils.boundsOnScreen(BoundsQueryUtils.bounds(scene), scene);
    }


    public BoundsQuery bounds(Window window) {
        return () -> BoundsQueryUtils.boundsOnScreen(BoundsQueryUtils.bounds(window), window);
    }


    public BoundsQuery bounds(String query) {
        throw new UnsupportedOperationException();
    }




    public <T extends Node> BoundsQuery bounds(Predicate<T> predicate) {
        throw new UnsupportedOperationException();
    }


    public FxRobot targetPos(Pos pointPosition) {
        context.setPointPosition(pointPosition);
        return this;
    }


    public PointQuery point(double x, double y) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(new Point2D(x, y)).atPosition(pointPosition);
    }


    public PointQuery point(Point2D point) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(point).atPosition(pointPosition);
    }


    public PointQuery point(Bounds bounds) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        return pointLocator.point(bounds).atPosition(pointPosition);
    }


    public PointQuery point(Node node) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(node.getScene().getWindow());
        return pointLocator.point(node).onNode(node).atPosition(pointPosition);
    }


    public PointQuery point(Scene scene) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(scene.getWindow());
        return pointLocator.point(scene).atPosition(pointPosition);
    }


    public PointQuery point(Window window) {
        PointLocator pointLocator = context.getPointLocator();
        Pos pointPosition = context.getPointPosition();
        targetWindow(window);
        return pointLocator.point(window).atPosition(pointPosition);
    }


    public PointQuery point(String query) {
        NodeQuery nodeQuery = lookup(query);
        Node node = queryNode(nodeQuery, "the query \"" + query + "\"");
        return point(node).atPosition(context.getPointPosition());
    }


    public <T extends Node> PointQuery point(Predicate<T> predicate) {
        NodeQuery nodeQuery = lookup(predicate);
        Node node = queryNode(nodeQuery, "the predicate");
        return point(node).atPosition(context.getPointPosition());
    }


    public PointQuery offset(Point2D point, double offsetX, double offsetY) {
        return point(point).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(Bounds bounds, double offsetX, double offsetY) {
        return point(bounds).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(Node node, double offsetX, double offsetY) {
        return point(node).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(Node node, Pos offsetReferencePos, double offsetX, double offsetY) {
        return point(node).atPosition(offsetReferencePos).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(Scene scene, double offsetX, double offsetY) {
        return point(scene).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(Window window, double offsetX, double offsetY) {
        return point(window).atOffset(offsetX, offsetY);
    }


    public PointQuery offset(String query, double offsetX, double offsetY) {
        return point(query).atOffset(offsetX, offsetY);
    }


    public <T extends Node> PointQuery offset(Predicate<T> predicate, double offsetX, double offsetY) {
        return point(predicate).atOffset(offsetX, offsetY);
    }


    public Capture capture(Rectangle2D screenRegion) {
        return () -> context.getCaptureSupport().captureRegion(screenRegion);
    }


    public Capture capture(Bounds bounds) {
        Rectangle2D region = new Rectangle2D(bounds.getMinX(), bounds.getMinY(),
                bounds.getWidth(), bounds.getHeight());
        return () -> context.getCaptureSupport().captureRegion(region);
    }


    public Capture capture(Node node) {
        return () -> context.getCaptureSupport().captureNode(node);
    }


    public Capture capture(Image image) {
        return () -> image;
    }


    public Capture capture(Path path) {
        return () -> context.getCaptureSupport().loadImage(path);
    }


    public Capture capture(URL url) {
        try {
            Path path = Paths.get(url.toURI());
            return () -> context.getCaptureSupport().loadImage(path);
        }
        catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }


    public FxRobot interact(Runnable runnable) {
        waitFor(asyncFx(runnable));
        waitForFxEvents();
        return this;
    }


    public <T> FxRobot interact(Callable<T> callable) {
        waitFor(asyncFx(callable));
        waitForFxEvents();
        return this;
    }


    public FxRobot interactNoWait(Runnable runnable) {
        waitFor(asyncFx(runnable));
        return this;
    }

    public <T> FxRobot interactNoWait(Callable<T> callable) {
        waitFor(asyncFx(callable));
        return this;
    }


    public FxRobot interrupt() {
        waitForFxEvents();
        return this;
    }


    public FxRobot interrupt(int attemptsCount) {
        waitForFxEvents(attemptsCount);
        return this;
    }


    public FxRobot push(KeyCode... combination) {
        context.getTypeRobot().push(combination);
        return this;
    }


    public FxRobot push(KeyCodeCombination combination) {
        context.getTypeRobot().push(combination);
        return this;
    }


    public FxRobot type(KeyCode... keyCodes) {
        context.getTypeRobot().type(keyCodes);
        return this;
    }


    public FxRobot type(KeyCode keyCode, int times) {
        context.getTypeRobot().type(keyCode, times);
        return this;
    }


    public FxRobot eraseText(int amount) {
        return type(KeyCode.BACK_SPACE, amount);
    }

    /**
     * @deprecated The implementation of this method simply pushes the keys ALT+F4 which
     * does not close the current window on all platforms.
     */
    public FxRobot closeCurrentWindow() {
        return push(KeyCode.ALT, KeyCode.F4).sleep(100);
    }


    public FxRobot write(char character) {
        context.getWriteRobot().write(character);
        return this;
    }


    public FxRobot write(String text) {
        context.getWriteRobot().write(text);
        return this;
    }


    public FxRobot write(String text, int sleepMillis) {
        context.getWriteRobot().write(text, sleepMillis);
        return this;
    }


    public FxRobot sleep(long milliseconds) {
        context.getSleepRobot().sleep(milliseconds);
        return this;
    }


    public FxRobot sleep(long duration, TimeUnit timeUnit) {
        context.getSleepRobot().sleep(duration, timeUnit);
        return this;
    }


    public FxRobot scroll(int amount, VerticalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }


    public FxRobot scroll(VerticalDirection direction) {
        scroll(1, direction);
        return this;
    }


    public FxRobot scroll(int amount, HorizontalDirection direction) {
        context.getScrollRobot().scroll(amount, direction);
        return this;
    }


    public FxRobot scroll(HorizontalDirection direction) {
        scroll(1, direction);
        return this;
    }


    public FxRobot press(KeyCode... keys) {
        context.getKeyboardRobot().press(keys);
        return this;
    }


    public FxRobot release(KeyCode... keys) {
        context.getKeyboardRobot().release(keys);
        return this;
    }


    public FxRobot press(MouseButton... buttons) {
        context.getMouseRobot().press(buttons);
        return this;
    }


    public FxRobot release(MouseButton... buttons) {
        context.getMouseRobot().release(buttons);
        return this;
    }

    /**
     * Convenience method: Moves mouse directly to the point returned from {@link #point(String)}, clicks the given
     * buttons, and returns itself for method chaining.
     */
    public FxRobot clickOn(String query, MouseButton... buttons) {
        return clickOn(query, Motion.DEFAULT, buttons);
    }


    public FxRobot clickOn(MouseButton... buttons) {
        context.getClickRobot().clickOn(buttons);
        return this;
    }


    public FxRobot clickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        context.getClickRobot().clickOn(pointQuery, motion, buttons);
        return this;
    }


    public FxRobot doubleClickOn(MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(buttons);
        return this;
    }


    public FxRobot doubleClickOn(PointQuery pointQuery, Motion motion, MouseButton... buttons) {
        context.getClickRobot().doubleClickOn(pointQuery, motion, buttons);
        return this;
    }


    public FxRobot clickOn(double x, double y, Motion motion, MouseButton... buttons) {
        return clickOn(point(x, y), motion, buttons);
    }


    public FxRobot clickOn(Point2D point, Motion motion, MouseButton... buttons) {
        return clickOn(point(point), motion, buttons);
    }


    public FxRobot clickOn(Bounds bounds, Motion motion, MouseButton... buttons) {
        return clickOn(point(bounds), motion, buttons);
    }


    public FxRobot clickOn(Node node, Motion motion, MouseButton... buttons) {
        return clickOn(point(node), motion, buttons);
    }


    public FxRobot clickOn(Scene scene, Motion motion, MouseButton... buttons) {
        return clickOn(point(scene), motion, buttons);
    }


    public FxRobot clickOn(Window window, Motion motion, MouseButton... buttons) {
        return clickOn(point(window), motion, buttons);
    }


    public FxRobot clickOn(String query, Motion motion, MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(query), motion, buttons);
    }


    public <T extends Node> FxRobot clickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons) {
        return clickOn(pointOfVisibleNode(predicate), motion, buttons);
    }

    public FxRobot rightClickOn() {
        return clickOn(MouseButton.SECONDARY);
    }

    public FxRobot rightClickOn(PointQuery pointQuery, Motion motion) {
        return clickOn(pointQuery, motion, MouseButton.SECONDARY);
    }

    public FxRobot rightClickOn(double x, double y, Motion motion) {
        return clickOn(x, y, motion, MouseButton.SECONDARY);
    }

    public FxRobot rightClickOn(Point2D point, Motion motion) {
        return clickOn(point, motion, MouseButton.SECONDARY);
    }


    public FxRobot rightClickOn(Bounds bounds, Motion motion) {
        return clickOn(bounds, motion, MouseButton.SECONDARY);
    }


    public FxRobot rightClickOn(Node node, Motion motion) {
        return clickOn(node, motion, MouseButton.SECONDARY);
    }


    public FxRobot rightClickOn(Scene scene, Motion motion) {
        return clickOn(scene, motion, MouseButton.SECONDARY);
    }


    public FxRobot rightClickOn(Window window, Motion motion) {
        return clickOn(window, motion, MouseButton.SECONDARY);
    }


    public FxRobot rightClickOn(String query, Motion motion) {
        return clickOn(query, motion, MouseButton.SECONDARY);
    }



    public <T extends Node> FxRobot rightClickOn(Predicate<T> predicate, Motion motion) {
        return clickOn(predicate, motion, MouseButton.SECONDARY);
    }


    public FxRobot doubleClickOn(double x, double y, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(x, y), motion, buttons);
    }


    public FxRobot doubleClickOn(Point2D point, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(point), motion, buttons);
    }


    public FxRobot doubleClickOn(Bounds bounds, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(bounds), motion, buttons);
    }


    public FxRobot doubleClickOn(Node node, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(node), motion, buttons);
    }


    public FxRobot doubleClickOn(Scene scene, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(scene), motion, buttons);
    }


    public FxRobot doubleClickOn(Window window, Motion motion, MouseButton... buttons) {
        return doubleClickOn(point(window), motion, buttons);
    }


    public FxRobot doubleClickOn(String query, Motion motion, MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(query), motion, buttons);
    }



    public <T extends Node> FxRobot doubleClickOn(Predicate<T> predicate, Motion motion, MouseButton... buttons) {
        return doubleClickOn(pointOfVisibleNode(predicate), motion, buttons);
    }


    public FxRobot drag(MouseButton... buttons) {
        context.getDragRobot().drag(buttons);
        return this;
    }


    public FxRobot drag(PointQuery pointQuery, MouseButton... buttons) {
        context.getDragRobot().drag(pointQuery, buttons);
        return this;
    }


    public FxRobot drop() {
        context.getDragRobot().drop();
        return this;
    }


    public FxRobot dropTo(PointQuery pointQuery) {
        context.getDragRobot().dropTo(pointQuery);
        return this;
    }


    public FxRobot dropBy(double x, double y) {
        context.getDragRobot().dropBy(x, y);
        return this;
    }


    public FxRobot drag(double x, double y, MouseButton... buttons) {
        return drag(point(x, y), buttons);
    }


    public FxRobot drag(Point2D point, MouseButton... buttons) {
        return drag(point(point), buttons);
    }


    public FxRobot drag(Bounds bounds, MouseButton... buttons) {
        return drag(point(bounds), buttons);
    }


    public FxRobot drag(Node node, MouseButton... buttons) {
        return drag(point(node), buttons);
    }


    public FxRobot drag(Scene scene, MouseButton... buttons) {
        return drag(point(scene), buttons);
    }


    public FxRobot drag(Window window, MouseButton... buttons) {
        return drag(point(window), buttons);
    }


    public FxRobot drag(String query, MouseButton... buttons) {
        return drag(pointOfVisibleNode(query), buttons);
    }



    public <T extends Node> FxRobot drag(Predicate<T> predicate, MouseButton... buttons) {
        return drag(pointOfVisibleNode(predicate), buttons);
    }

    public FxRobot dropTo(double x, double y) {
        return dropTo(point(x, y));
    }


    public FxRobot dropTo(Point2D point) {
        return dropTo(point(point));
    }


    public FxRobot dropTo(Bounds bounds) {
        return dropTo(point(bounds));
    }


    public FxRobot dropTo(Node node) {
        return dropTo(point(node));
    }


    public FxRobot dropTo(Scene scene) {
        return dropTo(point(scene));
    }


    public FxRobot dropTo(Window window) {
        return dropTo(point(window));
    }


    public FxRobot dropTo(String query) {
        return dropTo(pointOfVisibleNode(query));
    }


    public <T extends Node> FxRobot dropTo(Predicate<T> predicate) {
        return dropTo(pointOfVisibleNode(predicate));
    }


    public FxRobot moveTo(PointQuery pointQuery, Motion motion) {
        context.getMoveRobot().moveTo(pointQuery, motion);
        return this;
    }


    public FxRobot moveBy(double x, double y, Motion motion) {
        context.getMoveRobot().moveBy(x, y, motion);
        return this;
    }


    public FxRobot moveTo(double x, double y, Motion motion) {
        return moveTo(point(new Point2D(x, y)), motion);
    }


    public FxRobot moveTo(Point2D point, Motion motion) {
        return moveTo(point(point), motion);
    }


    public FxRobot moveTo(Bounds bounds, Motion motion) {
        return moveTo(point(bounds), motion);
    }


    public FxRobot moveTo(Node node, Pos offsetReferencePos, Point2D offset, Motion motion) {
        return moveTo(point(node).atPosition(offsetReferencePos).atOffset(offset), motion);
    }


    public FxRobot moveTo(Scene scene, Motion motion) {
        return moveTo(point(scene), motion);
    }


    public FxRobot moveTo(Window window, Motion motion) {
        return moveTo(point(window), motion);
    }


    public FxRobot moveTo(String query, Motion motion) {
        return moveTo(pointOfVisibleNode(query), motion);
    }

    public <T extends Node> FxRobot moveTo(Predicate<T> predicate, Motion motion) {
        return moveTo(pointOfVisibleNode(predicate), motion);
    }

    private PointQuery pointOfVisibleNode(String query) {
        NodeQuery nodeQuery = lookup(query);
        Node node = queryVisibleNode(nodeQuery, "the query \"" + query + "\"");
        return point(node);
    }



    private <T extends Node> PointQuery pointOfVisibleNode(Predicate<T> predicate) {
        NodeQuery nodeQuery = lookup(predicate);
        Node node = queryVisibleNode(nodeQuery, "the predicate");
        return point(node);
    }

    private Node queryNode(NodeQuery nodeQuery, String queryDescription) {
        Optional<Node> resultNode = nodeQuery.tryQuery();
        if (!resultNode.isPresent()) {
            throw new FxRobotException(queryDescription + " returned no nodes.");
        }
        return resultNode.get();
    }

    private Node queryVisibleNode(NodeQuery nodeQuery, String queryDescription) {
        Set<Node> resultNodes = nodeQuery.queryAll();
        if (resultNodes.isEmpty()) {
            throw new FxRobotException(queryDescription + " returned no nodes.");
        }
        Optional<Node> resultNode = from(resultNodes).match(isVisible()).tryQuery();
        if (!resultNode.isPresent()) {
            throw new FxRobotException(queryDescription + " returned " + resultNodes.size() + " nodes" +
                    ", but no nodes were visible.");
        }
        return resultNode.get();
    }
}
