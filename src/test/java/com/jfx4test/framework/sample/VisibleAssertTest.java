package com.jfx4test.framework.sample;

import com.jfx4test.framework.junit.ApplicationTest;
import com.jfx4test.framework.junit.FxAssertions;
import com.jfx4test.framework.junit.Start;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

@ApplicationTest
public class VisibleAssertTest {

    @Start
    void onStart(Stage stage) {
        Label label1 = new Label();
        label1.setId("sample1");
        label1.setText("unit test");
        Label label2 = new Label();
        label2.setId("sample2");
        label2.setText("unit test");
        label2.setVisible(false);
        stage.setScene(new Scene(new VBox(label1, label2), 100, 100));
        stage.show();
    }

    @Test
    public void visibleNot() {
        FxAssertions.assertVisiblyById("sample1");
        FxAssertions.assertNotVisiblyById("sample2");
    }
}
