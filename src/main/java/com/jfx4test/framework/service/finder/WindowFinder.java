package com.jfx4test.framework.service.finder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.jfx4test.framework.util.WindowUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WindowFinder {

    private Window lastTargetWindow;


    public Window targetWindow() {
        return lastTargetWindow;
    }


    public void targetWindow(Window window) {
        lastTargetWindow = window;
    }


    public void targetWindow(Predicate<Window> predicate) {
        targetWindow(window(predicate));
    }


   /*
    public List<Window> listWindows() {
        return fetchWindowsInQueue();
    }
*/

    public List<Window> listTargetWindows() {
        return fetchWindowsByProximityTo(lastTargetWindow);
    }


    public Window window(Predicate<Window> predicate) {
        return fetchWindowsByProximityTo(lastTargetWindow).stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }


    public void targetWindow(int windowIndex) {
        targetWindow(window(windowIndex));
    }


    public void targetWindow(String stageTitleRegex) {
        targetWindow(window(stageTitleRegex));
    }


    public void targetWindow(Pattern stageTitlePattern) {
        targetWindow(window(stageTitlePattern));
    }


    public void targetWindow(Scene scene) {
        targetWindow(window(scene));
    }


    public void targetWindow(Node node) {
        targetWindow(window(node));
    }


    public Window window(int windowIndex) {
        List<Window> windows = fetchWindowsByProximityTo(lastTargetWindow);
        return windows.get(windowIndex);
    }


    public Window window(String stageTitleRegex) {
        return window(hasStageTitlePredicate(stageTitleRegex));
    }


    public Window window(Pattern stageTitlePattern) {
        return window(hasStageTitlePredicate(stageTitlePattern.toString()));
    }


    public Window window(Scene scene) {
        return scene.getWindow();
    }


    public Window window(Node node) {
        return window(node.getScene());
    }

/*
  @SuppressWarnings("deprecation")
    private List<Window> fetchWindowsInQueue() {
        return Collections.unmodifiableList(WindowUtil.getWindows());
    }
*/

    private List<Window> fetchWindowsByProximityTo(Window targetWindow) {
        return orderWindowsByProximityTo(targetWindow, Window.getWindows());
    }

    private List<Window> orderWindowsByProximityTo(Window targetWindow, List<Window> windows) {
        List<Window> copy = new ArrayList<>(windows);
        copy.sort(Comparator.comparingInt(w -> calculateWindowProximityTo(targetWindow, w)));
        return Collections.unmodifiableList(copy);
    }

    private int calculateWindowProximityTo(Window targetWindow, Window window) {
        if (window == targetWindow) {
            return 0;
        }
        if (isOwnerOf(window, targetWindow)) {
            return 1;
        }
        return 2;
    }

    private boolean isOwnerOf(Window window, Window targetWindow) {
        Window ownerWindow = retrieveOwnerOf(window);
        if (ownerWindow == targetWindow) {
            return true;
        }
        return ownerWindow != null && isOwnerOf(ownerWindow, targetWindow);
    }

    private Window retrieveOwnerOf(Window window) {
        if (window instanceof Stage) {
            return ((Stage) window).getOwner();
        }
        if (window instanceof PopupWindow) {
            return ((PopupWindow) window).getOwnerWindow();
        }
        return null;
    }

    private Predicate<Window> hasStageTitlePredicate(String stageTitleRegex) {
        return window -> window instanceof Stage &&
                hasStageTitle((Stage) window, stageTitleRegex);
    }

    private boolean hasStageTitle(Stage stage, String stageTitleRegex) {
        return stage.getTitle() != null && stage.getTitle().matches(stageTitleRegex);
    }

}
