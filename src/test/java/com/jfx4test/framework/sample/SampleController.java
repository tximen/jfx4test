package com.jfx4test.framework.sample;


import javafx.fxml.FXML;

import java.util.logging.Logger;

public class SampleController {

    private static final Logger LOGGER = Logger.getLogger("com.jfx4test.framework.sample.SampleController");

    private boolean clicked;

    @FXML
    public void clickMe() {
        LOGGER.info("click me");
        this.clicked = true;
    }

    public boolean isClicked() {
        return clicked;
    }
}
