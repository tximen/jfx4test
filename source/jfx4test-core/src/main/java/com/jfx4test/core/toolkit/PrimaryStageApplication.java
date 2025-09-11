package com.jfx4test.framework.toolkit;
import java.util.concurrent.CompletableFuture;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main application used during tests when a developer is not testing his/her own subclass of {@link Application}.
 * The {@code primaryStage} from {@link Application#start(Stage)} can be accessed via {@link #PRIMARY_STAGE_FUTURE}.
 */
public class PrimaryStageApplication extends Application {

    public static final CompletableFuture<Stage> PRIMARY_STAGE_FUTURE = new CompletableFuture<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(getClass().getSimpleName());
        PRIMARY_STAGE_FUTURE.complete(primaryStage);
    }

}
