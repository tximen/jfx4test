package com.jfx4test.framework.toolkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfx4test.framework.toolkit.ApplicationLauncher;
import com.jfx4test.framework.toolkit.ApplicationService;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import static com.jfx4test.framework.util.WaitForAsyncUtils.async;
import static com.jfx4test.framework.util.WaitForAsyncUtils.asyncFx;

public class ToolkitService {

    private static final Logger LOGGER = Logger.getLogger("com.jfx4test.framework.toolkit.ToolkitService");


    private final ApplicationLauncher applicationLauncher;
    private final com.jfx4test.framework.toolkit.ApplicationService applicationService;

    public ToolkitService() {
        this (new ApplicationLauncher(), new com.jfx4test.framework.toolkit.ApplicationService());
    }

    public ToolkitService(ApplicationLauncher applicationLauncher,
                              ApplicationService applicationService) {
        this.applicationLauncher = applicationLauncher;
        this.applicationService = applicationService;
    }


    public Future<Stage> setupPrimaryStage(CompletableFuture<Stage> primaryStageFuture,
                                           Class<? extends Application> applicationClass,
                                           String... applicationArgs) {
        if (!primaryStageFuture.isDone()) {
            async(() -> {
                try {
                    applicationLauncher.launch(applicationClass, applicationArgs);
                }
                catch (Throwable exception) {
                    primaryStageFuture.completeExceptionally(exception);
                }
            });
        }
        return primaryStageFuture;
    }


    public Future<Void> setupFixture(Runnable runnable) {
        return asyncFx(runnable);
    }


    public <T> Future<T> setupFixture(Callable<T> callable) {
        return asyncFx(callable);
    }


    public Future<Stage> setupStage(Stage stage,
                                    Consumer<Stage> stageConsumer) {
        return asyncFx(() -> {
            stageConsumer.accept(stage);
            return stage;
        });
    }


    public Future<Scene> setupScene(Stage stage,
                                    Supplier<? extends Scene> sceneSupplier) {
        return asyncFx(() -> {
            Scene scene = sceneSupplier.get();
            stage.setScene(scene);
            return scene;
        });
    }


    public Future<Parent> setupSceneRoot(Stage stage,
                                         Supplier<? extends Parent> sceneRootSupplier) {
        return asyncFx(() -> {
            Parent rootNode = sceneRootSupplier.get();
            stage.setScene(new Scene(rootNode));
            return rootNode;
        });
    }


    public Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                                Class<? extends Application> applicationClass,
                                                String... applicationArgs) {
        return async(() -> {
            Application application = asyncFx(() -> createApplication(applicationClass)).get();
            registerApplicationParameters(application, applicationArgs);
            applicationService.init(application).get();
            applicationService.start(application, stageSupplier.get()).get();
            return application;
        });
    }


    public Future<Application> setupApplication(Supplier<Stage> stageSupplier,
                                                Supplier<Application> applicationSupplier,
                                                String... applicationArgs) {
        return async(() -> {
            Application application = asyncFx(applicationSupplier::get).get();
            registerApplicationParameters(application, applicationArgs);
            applicationService.init(application).get();
            applicationService.start(application, stageSupplier.get()).get();
            return application;
        });
    }


    public Future<Void> cleanupApplication(Application application) {
        cleanupParameters(application);
        return applicationService.stop(application);
    }

    private Application createApplication(Class<? extends Application> applicationClass) throws Exception {
        return applicationClass.getDeclaredConstructor().newInstance();
    }

    private void registerApplicationParameters(Application application, String... applicationArgs) {
        String type = "com.sun.javafx.application.ParametersImpl";
        String methodName = "registerParameters";

        try {
            // Use reflection to get the class, constructor and method
            Class<?> parametersImpl = getClass().getClassLoader().loadClass(type);
            Constructor<?> constructor = parametersImpl.getDeclaredConstructor(List.class);
            Method method =
                    parametersImpl.getDeclaredMethod(methodName, Application.class, Application.Parameters.class);

            // Create an instance of the ParametersImpl class
            Application.Parameters parameters =
                    (Application.Parameters)constructor.newInstance(Arrays.asList(applicationArgs));
            // Call the registerParameters on the ParametersImpl instance
            method.invoke(parametersImpl, application, parameters);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void cleanupParameters(Application application) {
        // This block removes the application parameters from ParametersImpl
        try {
            // Use reflection to get the ParametersImpl.params field
            Class<?> parametersImpl = com.sun.javafx.application.ParametersImpl.class;
            Field field = parametersImpl.getDeclaredField( "params");
            field.setAccessible(true);
            Map<Application, Application.Parameters> params;
            params = (Map<Application, Application.Parameters>) field.get(null);
            params.remove(application);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, exception, () -> "cleanupParameters failed");
            exception.printStackTrace();
        }
    }

}
