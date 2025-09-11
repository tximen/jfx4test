package com.jfx4test.framework.matcher;

import javafx.scene.control.Labeled;
import org.assertj.core.api.Assertions;

public record LabeledMatcher(Labeled nodeLabel) {

    public void hasText(String exceptedText) {
        Assertions.assertThat(nodeLabel.getText()).as(() -> info("text")).isEqualTo(exceptedText);
    }



    private String info(String message) {
        return "invalid %s on %s".formatted(message, this.nodeLabel);
    }
}
