package com.jfx4test.framework.fxml;
import com.jfx4test.framework.util.ApplicationFixture;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        try (InputStream fxmlStream = findFxmlResource().openStream()) {
            Scene scene = createScene(fxmlStream);
            if (Arrays.stream(this.fxmlSource.stylesheet()).anyMatch(this::validSheet)) {
                addStyleSheets(scene);
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            if (exception.getCause() instanceof FxmlFactoryException factoryException) {
                throw factoryException;
            } else {
                throw new UncheckedIOException("cannot create view %s".formatted(this.fxmlSource.sourcePath()), exception);
            }
        }
    }

    private void addStyleSheets(Scene scene) {
        ObservableList<String> styleSheets = scene.getStylesheets();
        Arrays.stream(this.fxmlSource.stylesheet())
              .filter(this::validSheet)
              .map(this::convertToExternal)
              .forEach(styleSheets::add);
    }

    private Scene createScene(InputStream fxmlStream) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(this.controllerFactory);
        if (this.fxmlSource.validWithAndHeight()) {
           return new Scene(loader.load(fxmlStream), this.fxmlSource.width(), this.fxmlSource.height());
        } else {
            return new Scene(loader.load(fxmlStream));
        }
    }

    private String convertToExternal(String sheetName) {
        URL resource = this.testInstance.getClass().getClassLoader().getResource(sheetName);
        String externalSheet = resource.toExternalForm();
        LOGGER.info("add stylesheet %s".formatted(externalSheet));
        return externalSheet;
    }

    private boolean validSheet(String sheetName) {
        if (sheetName == null || sheetName.isBlank()) {
            return false;
        } else {
           URL resource = this.testInstance.getClass().getClassLoader().getResource(sheetName);
           if (resource == null) {
               LOGGER.warning("no such style sheet %s".formatted(sheetName));
               return false;
           } else {
               return true;
           }
        }
    }

    private URL findFxmlResource() {
        Optional<URL> resource = findResource(this.fxmlSource.sourcePath());
        if (resource.isPresent()) {
            return resource.get();
        }
        resource = findResource("/%s".formatted(this.fxmlSource.sourcePath()));
        if (resource.isPresent()) {
            return resource.get();
        }
        throw new FxmlSourceMissingException("no such fxml resource [%s]".formatted(this.fxmlSource.sourcePath()));
    }


    private Optional<URL> findResource(String sourcePath) {
        LOGGER.info(() -> "load %s by current thread".formatted(sourcePath));
        URL resource = Thread.currentThread().getContextClassLoader().getResource(sourcePath);
        if (resource != null) {
            return Optional.of(resource);
        }
        LOGGER.info(() -> "load %s by system".formatted(sourcePath));
        resource = ClassLoader.getSystemClassLoader().getResource(sourcePath);
        if (resource != null) {
            return Optional.of(resource);
        }
        LOGGER.info(() -> "load %s by platform".formatted(sourcePath));
        resource = ClassLoader.getPlatformClassLoader().getResource(sourcePath);
        if (resource != null) {
            return Optional.of(resource);
        }
        LOGGER.info(() -> "load %s by %s".formatted(sourcePath,  this.testInstance.getClass().getName()));
        resource = this.testInstance.getClass().getResource(sourcePath);
        if (resource != null) {
            return Optional.of(resource);
        }
        LOGGER.info(() -> "load %s by ApplicationFxmlFixture.class".formatted(sourcePath));
        resource = ApplicationFxmlFixture.class.getResource(sourcePath);
        if (resource != null) {
            return Optional.of(resource);
        }
        return Optional.empty();
    }
}
