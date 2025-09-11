package com.jfx4test.framework.service.query;


import java.util.concurrent.Callable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class CallableBoundsPointQuery extends PointQueryBase {

    private final Callable<Bounds> callableBounds;

    public CallableBoundsPointQuery(Callable<Bounds> callableBounds) {
        this(callableBounds, null);
    }

    public CallableBoundsPointQuery(Callable<Bounds> callableBounds, Node node) {
        this.callableBounds = callableBounds;
        this.node = node;
    }

    @Override
    public Point2D query() {
        Bounds bounds = fetchCallableBounds();
        PointQuery boundsQuery = new BoundsPointQuery(bounds)
                .atPosition(getPosition())
                .atOffset(getOffset());
        return boundsQuery.query();
    }

    private Bounds fetchCallableBounds() {
        try {
            return callableBounds.call();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}

