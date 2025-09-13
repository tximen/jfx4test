package com.jfx4test.framework.sample;

import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.FxmlController;
import com.jfx4test.framework.junit.FxmlSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ApplicationExtension.class)
@FxmlSource("fxml/sample.fxml")
public class SampleFxmlApp3Test {

    private final SampleController controller = new SampleController();

    @FxmlController
    public SampleController createController() {
        return this.controller;
    }

    @Test
    public void click_me(FxRobot robot) {
        Assertions.assertThat(this.controller.isClicked()).isFalse();
        robot.clickById("clickMe");
        Assertions.assertThat(this.controller.isClicked()).isTrue();
    }
}
