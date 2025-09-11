package com.jfx4test.framework.junit6;

import com.jfx4test.framework.api.FxAssertions;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.jfx4test.framework.api.FxRobot;



@ExtendWith(JavaFxApplicationExtension.class)
class ApplicationRuleTest {

   @Start
   void onStart(Stage stage) {
      Button button = new Button("click me!");
      button.setId("button");
      button.setOnAction(_ -> button.setText("clicked!"));
      stage.setScene(new Scene(new StackPane(button), 100, 100));
      stage.show();
   }

   @Test
   void should_contain_button() {
       FxAssertions.assertLabeledById("button").hasText("click me!");
   }

   @Test
   void should_click_on_button(FxRobot robot) {
       FxAssertions.assertLabeledById("button").hasText("click me!");
       robot.clickOn(".button");
       FxAssertions.assertLabeledById("button").hasText("clicked!");
    }

}
