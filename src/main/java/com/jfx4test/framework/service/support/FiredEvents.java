package com.jfx4test.framework.service.support;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;

/**
 * Stores a list of events that have been fired since the start of a test; useful for debugging.
 * Use {@link #beginStoringFiredEventsOf(Stage)} to start storing a stage's fired events and
 * {@link #stopStoringFiredEvents()} when finished and cleaning up this object.
 */
public final class FiredEvents {

    public static FiredEvents beginStoringFiredEventsOf(Stage stage) {
        return new FiredEvents(stage);
    }

    private final List<Event> events = new LinkedList<>();
    private final Runnable removeListener;

    private FiredEvents(Stage stage) {
        if (stage == null) {
            removeListener = null;
        } else {
            EventHandler<Event> addFiredEvent = events::add;
            stage.addEventFilter(EventType.ROOT, addFiredEvent);
            removeListener = () -> stage.removeEventFilter(EventType.ROOT, addFiredEvent);
        }
    }

    public final List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public final void clearEvents() {
        events.clear();
    }

    public final void stopStoringFiredEvents() {
        if (removeListener != null) {
            removeListener.run();
        }
    }

}
