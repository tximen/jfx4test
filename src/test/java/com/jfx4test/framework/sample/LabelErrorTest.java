package com.jfx4test.framework.sample;

import com.jfx4test.framework.junit.FxAssertions;
import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.Start;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;

@ExtendWith(ApplicationExtension.class)
public class LabelErrorTest {

    @Start
    void onStart(Stage stage) {
        stage.setScene(new Scene(createBox() , 100, 100));
        stage.show();
    }

    private VBox createBox() {
        return
           new VBox(createLabel("label01", "unit 1"),
                    createLabel("label02", "unit 2"));
    }

    private Label createLabel(String id, String message) {
        Label label = new Label();
        label.setId(id);
        label.setText(message);
        return label;
    }


    @Test
    void test_no_such_node_error() {
        Assertions.assertThatThrownBy(() -> FxAssertions.assertLabeledById("xx"))
                  .isInstanceOfAny(AssertionFailedError.class)
                  .hasMessageContaining("** no such node **");
    }
}
