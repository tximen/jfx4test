package com.jfx4test.framework.junit6;

import com.jfx4test.framework.api.FxAssertions;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JavaFxApplicationExtension.class)
public class LabelAssertionTest {

    @Start
    void onStart(Stage stage) {
        Label label = new Label();
        label.setId("sample");
        label.setText("unit test");

        stage.setScene(new Scene(new StackPane(label), 100, 100));
        stage.show();
    }

    @Test
    void sample_label() {
        System.out.println("test: "  + FxAssertions.assertLabelById("sample"));


        // expect:
        //   assertThat(".button", hasText("click me!"));
    }
}
