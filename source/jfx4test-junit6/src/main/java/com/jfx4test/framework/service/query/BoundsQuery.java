package com.jfx4test.framework.service.query;

import javafx.geometry.Bounds;

/**
 * Essentially, a {@link java.util.function.Supplier} that returns a {@link Bounds} object via {@link #query()}.
 */
@FunctionalInterface
public interface BoundsQuery {

    Bounds query();

}
