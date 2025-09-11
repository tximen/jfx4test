package com.jfx4test.framework.api;

import javafx.scene.Node;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NoSuchLabelSupplier implements Supplier<String> {

    private static final String SEPERATOR = "\n     ";

    private final String nodeId;

    public NoSuchLabelSupplier(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String get() {
        return """
                ** no such node **
                   id=%s
                   available nodes {
                     %s
                   }""".formatted(this.nodeId, findAllNodes().stream().map(this::nodeInfo).collect(Collectors.joining(SEPERATOR)));

    }

    private String nodeInfo(Node node) {
        return node.toString();
    }

    private Set<Node> findAllNodes() {
        return FxApiFxApiContextHolder
                .getInstance()
                .getApiContext()
                .nodeFinder()
                .lookup(this::nodeWithValidID)
                .queryAll();
    }

    private boolean nodeWithValidID(Node node) {
        return node != null && node.getId() != null;
    }
}
