package com.jfx4test.framework.service.support;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import com.jfx4test.framework.util.ColorUtils;

public class PixelMatcher {

    private final double colorBlendFactor;
    private final double minColorDistSq;

    public PixelMatcher() {
        this(0.20, 0.75);
    }

    public PixelMatcher(double minColorDistFactor, double colorBlendFactor) {
        this.colorBlendFactor = colorBlendFactor;
        double maxColorDistSq = ColorUtils.calculateColorDistSq(Color.BLACK, Color.WHITE);
        this.minColorDistSq = maxColorDistSq * (minColorDistFactor * minColorDistFactor);
    }

    public PixelMatcherResult match(Image image0,
                                    Image image1) {

        WritableImage matchImage = createEmptyMatchImage(image0, image1);
        int imageWidth = (int) matchImage.getWidth();
        int imageHeight = (int) matchImage.getHeight();

        long matchPixels = 0L;
        long totalPixels = imageWidth * imageHeight;

        for (int imageY = 0; imageY < imageHeight; imageY += 1) {
            for (int imageX = 0; imageX < imageWidth; imageX += 1) {
                Color color0 = image0.getPixelReader().getColor(imageX, imageY);
                Color color1 = image1.getPixelReader().getColor(imageX, imageY);
                boolean areColorsMatching = matchColors(color0, color1);

                if (areColorsMatching) {
                    matchPixels += 1;
                    Color matchColor = createMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, matchColor);
                }
                else {
                    Color nonMatchColor = createNonMatchColor(color0, color1);
                    matchImage.getPixelWriter().setColor(imageX, imageY, nonMatchColor);
                }
            }
        }

        return new PixelMatcherResult(matchImage, matchPixels, totalPixels);
    }

    public WritableImage createEmptyMatchImage(Image image0,
                                               Image image1) {
        return new WritableImage((int) image0.getWidth(), (int) image1.getHeight());
    }

    public boolean matchColors(Color color0, Color color1) {
        double colorDistSq = ColorUtils.calculateColorDistSq(color0, color1);
        return colorDistSq < minColorDistSq;
    }

    /**
     * Creates a color that represents a mismatch between the two images' pixels.
     */
    public Color createNonMatchColor(Color color0, Color color1) {
        return Color.RED;
    }


    public Color createMatchColor(Color color0, Color color1) {
        double gray = color0.grayscale().getRed();
        double opacity = color0.getOpacity();
        return Color.gray(blendToWhite(gray, colorBlendFactor), opacity);
    }

    private double blendToWhite(double gray, double factor) {
        return ((1.0 - factor) * gray) + factor;
    }

}
