package com.jfx4test.framework.service.support;

import javafx.scene.image.Image;

/**
 * Indicates how similar/dissimilar two images were on a pixel-to-pixel comparison level via
 * {@link PixelMatcher#match(Image, Image)}.
 */
// TODO convert  this into a record
public class PixelMatcherResult {


    private final Image matchImage;
    private final long totalPixels;
    private final long matchPixels;
    private final double matchFactor;

    public PixelMatcherResult(Image matchImage, long matchPixels, long totalPixels) {
        this.matchImage = matchImage;
        this.totalPixels = totalPixels;
        this.matchPixels = matchPixels;
        this.matchFactor = matchPixels / (double) totalPixels;
    }

    /**
     * Gets the image whose pixels indicate matches and mismatches between the two original images.
     */
    public Image getMatchImage() {
        return matchImage;
    }

    /**
     * Gets the total number of pixels in the match image.
     */
    public long getTotalPixels() {
        return totalPixels;
    }

    /**
     * Gets the total number of pixels that matched between the two original images.
     */
    public long getMatchPixels() {
        return matchPixels;
    }

    /**
     * Gets the total number of pixels that did not match between the two original images.
     */
    public long getNonMatchPixels() {
        return totalPixels - matchPixels;
    }

    /**
     * Gets the percentage of pixels that matched between the two original images.
     */
    public double getMatchFactor() {
        return matchFactor;
    }

    /**
     * Gets the percentage of pixels that did not match between the two original images.
     */
    public double getNonMatchFactor() {
        return 1.0 - matchFactor;
    }

}
