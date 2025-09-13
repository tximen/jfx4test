package com.jfx4test.framework.sample;

import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.FxAssertions;
import com.jfx4test.framework.junit.FxmlController;
import com.jfx4test.framework.junit.FxmlControllerFactory;
import com.jfx4test.framework.junit.FxmlSource;
import com.jfx4test.framework.junit.Init;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ApplicationExtension.class)
@FxmlSource("fxml/sample.fxml")
public class SampleFxmlApp2Test {

    @FxmlController
    private SampleController controller;

    @Init
    public void createController() {
        this.controller = new SampleController();
    }

    @Test
    public void click_me(FxRobot robot) {
        Assertions.assertThat(this.controller.isClicked()).isFalse();
        robot.clickById("clickMe");
        Assertions.assertThat(this.controller.isClicked()).isTrue();
    }
}


