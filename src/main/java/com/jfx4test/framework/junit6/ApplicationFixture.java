package com.jfx4test.framework.junit6;

import javafx.stage.Stage;

public interface ApplicationFixture {

    void init() throws Exception;

    void start(Stage stage) throws Exception;

    void stop() throws Exception;
}

