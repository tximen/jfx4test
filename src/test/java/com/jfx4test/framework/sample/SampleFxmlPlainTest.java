package com.jfx4test.framework.sample;

import com.jfx4test.framework.junit.ApplicationExtension;
import com.jfx4test.framework.junit.Start;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@ExtendWith(ApplicationExtension.class)
public class SampleFxmlPlainTest {

    @Start
    void onStart(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        try (InputStream fxmlStream = SampleFxmlPlainTest.class.getResourceAsStream("/fxml/sample.fxml")) {
            stage.setScene(new Scene(loader.load(fxmlStream), 100, 100));
            stage.show();
        } catch (IOException exception) {
            throw new UncheckedIOException("cannot load panel", exception);
        }
    }

    @Test
    public void load_sample() {
        System.err.println("looad");
    }
}
