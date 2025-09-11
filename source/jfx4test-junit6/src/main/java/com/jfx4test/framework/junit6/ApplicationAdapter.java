package com.jfx4test.framework.junit6;

import javafx.application.Application;
import javafx.stage.Stage;

public final class ApplicationAdapter extends Application {

    private final ApplicationFixture applicationFixture;

    public ApplicationAdapter(ApplicationFixture applicationFixture) {
        this.applicationFixture = applicationFixture;
    }

    @Override
    public void init() throws Exception {
        applicationFixture.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        applicationFixture.start(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        applicationFixture.stop();
    }

    @Override
    public int hashCode() {
        return applicationFixture.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ApplicationAdapter otherAdapter
            && applicationFixture.equals(otherAdapter.applicationFixture);
    }

}

