package com.jfx4test.framework.service.query;

import com.jfx4test.framework.util.PointQueryUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

public class BoundsPointQuery extends PointQueryBase {

    private Bounds bounds;

    public BoundsPointQuery(Bounds bounds) {
        this.bounds = bounds;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public Point2D query() {
        Point2D point = PointQueryUtils.atPositionFactors(bounds, getPosition());
        Point2D offset = getOffset();
        return new Point2D(point.getX() + offset.getX(), point.getY() + offset.getY());
    }

}
