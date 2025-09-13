package com.jfx4test.framework.fxml;
import com.jfx4test.framework.util.ApplicationFixture;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class ApplicationFxmlFixture extends ApplicationFixture {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFxmlFixture.class.getName());

    private final FxmlConfig fxmlSource;
    private final Callback<Class<?>, Object> controllerFactory;

    public ApplicationFxmlFixture(Object testInstance, FxmlConfig fxmlSource, Callback<Class<?>, Object> controllerFactory,
                                  List<Method> init, List<Method> stop) {
        super(testInstance, init, stop);
        this.fxmlSource = fxmlSource;
        this.controllerFactory = controllerFactory;
    }

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(this.controllerFactory);
        try (InputStream fxmlStream = findFxmlResource().openStream()) {
            if (this.fxmlSource.validWithAndHeight()) {
                stage.setScene(new Scene(loader.load(fxmlStream), this.fxmlSource.width(), this.fxmlSource.height()));
            } else {
                stage.setScene(new Scene(loader.load(fxmlStream)));
            }
            stage.show();
        } catch (IOException exception) {
            if (exception.getCause() instanceof FxmlFactoryException factoryException) {
                throw factoryException;
            } else {
                throw new UncheckedIOException("cannot create view %s".formatted(this.fxmlSource.sourcePath()), exception);
            }
        }
    }

    private URL findFxmlResource() {
        URL resource =  this.testInstance.getClass().getClassLoader().getResource(this.fxmlSource.sourcePath());
        if (resource == null) {
            throw new FxmlSourceMissingException("no such fxml resource [%s]".formatted(this.fxmlSource.sourcePath()));
        } else {
            LOGGER.info("use fxml %s".formatted(resource.getFile()));
            return resource;
        }
    }

}
