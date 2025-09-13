package com.jfx4test.framework.sample;

import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.ControllerFactory;
import com.jfx4test.framework.junit.FxmlSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ApplicationExtension.class)
@FxmlSource("fxml/sample.fxml")
public class SampleFxmlApp1Test {


    @Test
    public void load_sample() {
        System.err.println("looad");
    }

    @ControllerFactory
    public Object controller(Class<?> reference) {
        if (SampleController.class.equals(reference)) {
            return new SampleController();
        } else {
            throw new RuntimeException("not implemented");
        }

    }
}
