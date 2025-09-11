package com.jfx4test.framework.junit6;

import com.jfx4test.framework.api.FxApiFxApiContextHolder;
import com.jfx4test.framework.service.query.NodeQuery;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.jfx4test.framework.api.FxRobot;

import static com.jfx4test.framework.api.FxAssert.assertThat;
//import static com.jfx4test.framework.matcher.control.LabeledMatchers.hasText;


@ExtendWith(JavaFxApplicationExtension.class)
class ApplicationRuleTest {

   @Start
   void onStart(Stage stage) {
      Button button = new Button("click me!");
      button.setOnAction(actionEvent -> button.setText("clicked!"));
      stage.setScene(new Scene(new StackPane(button), 100, 100));
      stage.show();
   }

   @Test
   void should_contain_button() {

      NodeQuery query = FxApiFxApiContextHolder.getInstance().getApiContext().nodeFinder().lookup(".button");
      query.query();
      System.err.println("query: " + query.query());
      // expect:
    //   assertThat(".button", hasText("click me!"));
   }

   @Test
   void should_click_on_button(FxRobot robot) {
      // when:
     robot.clickOn(".button");

      // then:
    //  verifyThat(".button", hasText("clicked!"));

    }

}
