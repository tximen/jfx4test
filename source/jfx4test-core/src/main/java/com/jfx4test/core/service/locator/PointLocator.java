package com.jfx4test.framework.service.locator;

import com.jfx4test.framework.service.query.CallableBoundsPointQuery;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.jfx4test.framework.service.query.BoundsPointQuery;
import com.jfx4test.framework.service.query.PointQuery;

public class PointLocator {

    private final BoundsLocator boundsLocator;

    public PointLocator(BoundsLocator boundsLocator) {
        this.boundsLocator = boundsLocator;
    }


    public PointQuery point(Bounds bounds) {
        return new BoundsPointQuery(bounds);
    }

    public PointQuery point(Point2D point) {
        return new BoundsPointQuery(new BoundingBox(point.getX(), point.getY(), 0, 0));
    }


    // TODO  create simple PointQuery instead of callable
    public PointQuery point(Node node) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(node), node);
    }


    public PointQuery point(Scene scene) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(scene));
    }


    public PointQuery point(Window window) {
        return new CallableBoundsPointQuery(() -> boundsLocator.boundsOnScreenFor(window));
    }

}
