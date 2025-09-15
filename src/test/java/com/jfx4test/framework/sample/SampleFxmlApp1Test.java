package com.jfx4test.framework.sample;

import com.jfx4test.framework.api.FxRobot;
import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.FxmlControllerFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ApplicationExtension.class)
public class SampleFxmlApp1Test {

    private final SampleController controller = new SampleController();

    @FxmlControllerFactory
    public Object controller(Class<?> reference) {
        if (SampleController.class.equals(reference)) {
            return this.controller;
        } else {
            throw new RuntimeException("not implemented");
        }
    }

    @Test
    public void click_me(FxRobot robot) {
        Assertions.assertThat(this.controller.isClicked()).isFalse();
        robot.clickById("clickMe");
        Assertions.assertThat(this.controller.isClicked()).isTrue();
    }
}
