package com.jfx4test.framework.service.locator;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

public class BoundsLocator {


    public Bounds boundsInSceneFor(Node node) {
        Bounds sceneBounds = node.localToScene(node.getBoundsInLocal());
        return limitToVisibleBounds(sceneBounds, node.getScene());
    }


    public Bounds boundsInWindowFor(Scene scene) {
        return new BoundingBox(scene.getX(), scene.getY(), scene.getWidth(), scene.getHeight());
    }


    public Bounds boundsInWindowFor(Bounds boundsInScene, Scene scene) {
        Bounds visibleBoundsInScene = limitToVisibleBounds(boundsInScene, scene);
        Bounds windowBounds = boundsInWindowFor(scene);
        return translateBounds(visibleBoundsInScene, windowBounds.getMinX(), windowBounds.getMinY());
    }


    public Bounds boundsOnScreenFor(Node node) {
        Bounds sceneBounds = boundsInSceneFor(node);
        return boundsOnScreenFor(sceneBounds, node.getScene());
    }


    public Bounds boundsOnScreenFor(Scene scene) {
        Bounds windowBounds = boundsInWindowFor(scene);
        Window window = scene.getWindow();
        return translateBounds(windowBounds, window.getX(), window.getY());
    }


    public Bounds boundsOnScreenFor(Window window) {
        return new BoundingBox(
                window.getX(), window.getY(),
                window.getWidth(), window.getHeight()
        );
    }

    public Bounds boundsOnScreenFor(Bounds boundsInScene, Scene scene) {
        Bounds windowBounds = boundsInWindowFor(boundsInScene, scene);
        Window window = scene.getWindow();
        return translateBounds(windowBounds, window.getX(), window.getY());
    }

    private Bounds limitToVisibleBounds(Bounds boundsInScene, Scene scene) {
        Bounds sceneBounds = new BoundingBox(0, 0, scene.getWidth(), scene.getHeight());
        Bounds visibleBounds = intersectBounds(boundsInScene, sceneBounds);
        if (!areBoundsVisible(visibleBounds)) {
            throw new BoundsLocatorException("bounds are not visible in Scene");
        }
        return visibleBounds;
    }

    private Bounds intersectBounds(Bounds a, Bounds b) {
        double minX = Math.max(a.getMinX(), b.getMinX());
        double minY = Math.max(a.getMinY(), b.getMinY());
        double maxX = Math.min(a.getMaxX(), b.getMaxX());
        double maxY = Math.min(a.getMaxY(), b.getMaxY());
        double width = maxX - minX;
        double height = maxY - minY;
        return new BoundingBox(minX, minY, width, height);
    }

    private boolean areBoundsVisible(Bounds bounds) {
        return bounds.getWidth() >= 0 && bounds.getHeight() >= 0; //TODO always true...
    }

    private Bounds translateBounds(Bounds bounds, double x, double y) {
        return new BoundingBox(bounds.getMinX() + x, bounds.getMinY() + y,
                bounds.getWidth(), bounds.getHeight());
    }

}
