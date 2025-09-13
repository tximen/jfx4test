package com.jfx4test.framework.junit;
import com.jfx4test.framework.fxml.FxmlFactoryException;
import com.jfx4test.framework.fxml.FxmlSourceMissingException;
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

    private static final Logger LOGGER = Logger.getLogger("com.jfx4test.framework.fxml.FxmlApplicationFixture");


    private final FxmlSource fxmlSource;
    private final Callback<Class<?>, Object> controllerFactory;

    public ApplicationFxmlFixture(Object testInstance, FxmlSource fxmlSource, Callback<Class<?>, Object> controllerFactory,
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
            stage.setScene(new Scene(loader.load(fxmlStream), this.fxmlSource.width(), this.fxmlSource.height()));
            stage.show();
        } catch (IOException exception) {
            if (exception.getCause() instanceof FxmlFactoryException factoryException) {
                throw factoryException;
            } else {
                throw new UncheckedIOException("cannot create view %s".formatted(this.fxmlSource.value()), exception);
            }
        }
    }

    private URL findFxmlResource() {
        URL resource =  this.testInstance.getClass().getClassLoader().getResource(this.fxmlSource.value());
        if (resource == null) {
            throw new FxmlSourceMissingException("no such fxml resource [%s]".formatted(this.fxmlSource.value()));
        } else {
            LOGGER.info("use fxml %s".formatted(resource.getFile()));
            return resource;
        }
    }

}
