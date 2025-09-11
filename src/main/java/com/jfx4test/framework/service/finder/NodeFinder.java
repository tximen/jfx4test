package com.jfx4test.framework.service.finder;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.jfx4test.framework.service.finder.WindowFinder;
import com.jfx4test.framework.service.query.NodeIdPredicate;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import com.jfx4test.framework.service.query.NodeQuery;

import com.jfx4test.framework.util.NodeQueryUtils;


public class NodeFinder {


    private final WindowFinder windowFinder;

    public NodeFinder() {
        this.windowFinder = new WindowFinder();
    }

    public NodeFinder(WindowFinder windowFinder) {
        this.windowFinder = windowFinder;
    }

    public NodeQuery lookup(String query) {
        return fromAll().lookup(query);
    }

    public NodeQuery lookupById(String query) {
        return fromAll().lookup(new NodeIdPredicate(query));
    }

    public <T extends Node> NodeQuery lookup(Predicate<T> predicate) {
        return fromAll().lookup(predicate);
    }


    public NodeQuery fromAll() {
        return new NodeQuery().from(rootsOfWindows());
    }


    public NodeQuery from(Node... parentNodes) {
        return new NodeQuery().from(parentNodes);
    }


    public NodeQuery from(Collection<Node> parentNodes) {
        return new NodeQuery().from(parentNodes);
    }


    public NodeQuery from(NodeQuery nodeQuery) {
        return new NodeQuery().from(nodeQuery.queryAll());
    }


    public Node rootNode(Window window) {
        return window.getScene().getRoot();
    }


    public Node rootNode(Scene scene) {
        return scene.getRoot();
    }


    public Node rootNode(Node node) {
        return node.getScene().getRoot();
    }

    private Set<Node> rootsOfWindows() {
        List<Window> windows = windowFinder.listTargetWindows();
        return NodeQueryUtils.rootsOfWindows(windows);
    }

    public WindowFinder getWindowFinder() {
        return windowFinder;
    }
}
