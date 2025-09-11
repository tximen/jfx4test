package com.jfx4test.framework.api;

import com.jfx4test.framework.matcher.LabeledMatcher;
import com.jfx4test.framework.service.query.NodeIdPredicate;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class FxAssertions {

    public static LabeledMatcher assertLabeledById(String labelID) {
        Optional<Node> node = lookUpNodeById(labelID);
        Assertions.assertThat(node.isPresent()).as(new NoSuchLabelSupplier(labelID)).isTrue();
        Assertions.assertThat(node.get()).isInstanceOfAny(Labeled.class);
        if (node.get() instanceof Labeled nodeLabel) {
           return new LabeledMatcher(nodeLabel);
        } else {
           // this should not happen
           throw new IllegalStateException("node %s is not an instanceof Labeled".formatted(node.get()));
        }
    }

    private static Optional<Node> lookUpNodeById(String labelID) {
        return FxApiFxApiContextHolder
                 .getInstance()
                 .getApiContext()
                 .nodeFinder()
                 .lookup(new NodeIdPredicate(labelID))
                 .query();
    }


}
