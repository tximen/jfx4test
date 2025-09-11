package com.jfx4test.framework.service.query;

import javafx.scene.Node;

import java.util.function.Predicate;

public class NodeIdPredicate implements Predicate<Node> {

    private final String searchId;

    public NodeIdPredicate(String searchId) {
        this.searchId = searchId;
    }

    @Override
    public boolean test(Node node) {
        return this.searchId.equals(node.getId());
    }
}
