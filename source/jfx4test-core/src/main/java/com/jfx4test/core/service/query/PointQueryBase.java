package com.jfx4test.framework.service.query;

import java.util.Optional;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;

import com.jfx4test.framework.robot.Motion;
import com.jfx4test.framework.util.PointQueryUtils;

/**
 * TODO refactor replace interface PointQuery with this class
 */
public abstract class PointQueryBase implements PointQuery {

    private Point2D position = new Point2D(0, 0);
    private Point2D offset = new Point2D(0, 0);
    protected Node node;

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public Point2D getOffset() {
        return offset;
    }

    @Override
    public PointQuery atPosition(Point2D position) {
        this.position = position;
        return this;
    }

    @Override
    public PointQuery atPosition(double positionX, double positionY) {
        return atPosition(new Point2D(positionX, positionY));
    }

    @Override
    public PointQuery atPosition(Pos position) {
        return atPosition(PointQueryUtils.computePositionFactors(position));
    }

    @Override
    public PointQuery atOffset(Point2D offset) {
        this.offset = new Point2D(this.offset.getX() + offset.getX(), this.offset.getY() + offset.getY());
        return this;
    }

    @Override
    public PointQuery atOffset(double offsetX, double offsetY) {
        return atOffset(new Point2D(offsetX, offsetY));
    }

    @Override
    public PointQuery onNode(Node node) {
        this.node = node;
        return this;
    }

    @Override
    public Optional<Motion> queryMotion() {
        if (node == null) {
            return Optional.empty();
        }
        switch (node.getClass().getSimpleName()) {
            case "MenuItemContainer":
                return Optional.of(Motion.VERTICAL_FIRST);
            default:
                return Optional.of(Motion.DEFAULT);
        }
    }

    @Override
    public String toString() {
        return String.format("PointQueryBase [position = %s, offset = %s node = %s]", position, offset, node);
    }
}

