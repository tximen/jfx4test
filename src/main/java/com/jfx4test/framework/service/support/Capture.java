package com.jfx4test.framework.service.support;

import javafx.scene.image.Image;

/**
 * Essentially a {@link java.util.function.Supplier} that returns an {@link Image} via {@link #getImage()}.
 */
@FunctionalInterface
public interface Capture {

    Image getImage();

}
