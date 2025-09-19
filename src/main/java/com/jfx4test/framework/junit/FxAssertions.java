package com.jfx4test.framework.junit;

import com.jfx4test.framework.api.FxApiFxApiContextHolder;
import com.jfx4test.framework.api.NoSuchLabelSupplier;
import com.jfx4test.framework.matcher.LabeledMatcher;
import com.jfx4test.framework.service.query.NodeIdPredicate;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class FxAssertions {

    public static LabeledMatcher assertLabeledById(String labelID) {
        Node node = findNodeById(labelID);
        Assertions.assertThat(node).isInstanceOfAny(Labeled.class);
        if (node instanceof Labeled nodeLabel) {
           return new LabeledMatcher(nodeLabel);
        } else {
           // this should not happen
           throw new IllegalStateException("node %s is not an instanceof Labeled".formatted(node));
        }
    }

    public static void assertVisiblyById(String nodeID) {
        Assertions.assertThat(findNodeById(nodeID).isVisible()).isTrue();
    }

    public static void assertNotVisiblyById(String nodeID) {
        Assertions.assertThat(findNodeById(nodeID).isVisible()).isFalse();
    }

    private static Node findNodeById(String nodeID) {
        Optional<Node> node = lookUpNodeById(nodeID);
        Assertions.assertThat(node.isPresent()).as(new NoSuchLabelSupplier(nodeID)).isTrue();
        return node.get();
    }

    private static Optional<Node> lookUpNodeById(String nodeID) {
        return FxApiFxApiContextHolder
                 .getInstance()
                 .getApiContext()
                 .nodeFinder()
                 .lookup(new NodeIdPredicate(nodeID))
                 .query();
    }


}
