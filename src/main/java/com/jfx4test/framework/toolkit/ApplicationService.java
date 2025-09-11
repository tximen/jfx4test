package com.jfx4test.framework.toolkit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;



import static com.jfx4test.framework.util.WaitForAsyncUtils.asyncFx;

public class ApplicationService {

    private static final Logger LOGGER = Logger.getLogger("com.jfx4test.framework.toolkit.ApplicationService");

    public Future<Void> init(Application application) {
        // Should be called in TestFX launcher thread.
        CompletableFuture<Void> future = new CompletableFuture<>();
        try {
            application.init();
            future.complete(null);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception, () -> "app initialization failed");
            future.completeExceptionally(exception);
        }
        return future;
    }


    public Future<Void> start(Application application,
                              Stage targetStage) {
        // Should run in JavaFX application thread.
        return asyncFx(() -> {
            application.start(targetStage);
            return null;
        });
    }


    public Future<Void> stop(Application application) {
        // Should run in JavaFX application thread.
        return asyncFx(() -> {
            application.stop();
            return null;
        });
    }
}
