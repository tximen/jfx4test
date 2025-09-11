package com.jfx4test.framework.service.support;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;

import com.jfx4test.framework.robot.BaseRobot;

import static com.jfx4test.framework.util.WaitForAsyncUtils.asyncFx;
import static com.jfx4test.framework.util.WaitForAsyncUtils.waitFor;

public class CaptureSupport {

    public static final CaptureFileFormat DEFAULT_FORMAT = CaptureFileFormat.PNG;

    private final BaseRobot baseRobot;

    public CaptureSupport() {
        this.baseRobot = new BaseRobot();
    }

    public CaptureSupport(BaseRobot baseRobot) {
        this.baseRobot = baseRobot;
    }


    public Image captureNode(Node node) {
        return waitFor(asyncFx(() -> snapshotNodeToImage(node)));
    }


    public Image captureRegion(Rectangle2D region) {
        return baseRobot.captureRegion(region);
    }


    public Image loadImage(Path path) {
        checkFileExists(path);
        try (InputStream inputStream = Files.newInputStream(path)) {
            return readImageFromStream(inputStream);
        } catch (IOException exception) {
            throw new UncheckedIOException("cannot load image %s".formatted(path), exception);
        }
    }


    public void saveImage(Image image,
                          Path path) {
        saveImage(image, DEFAULT_FORMAT, path);
    }


    public void saveImage(Image image, CaptureFileFormat format, Path path) {
        checkParentDirectoryExists(path);
        try (OutputStream outputStream = Files.newOutputStream(path)) {
            writeImageToStream(image, format.toString(), outputStream);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public Image annotateImage(Shape shape,
                               Image image) {
        throw new UnsupportedOperationException();
    }


    public PixelMatcherResult matchImages(Image image0,
                                          Image image1,
                                          PixelMatcher pixelMatcher) {
        return pixelMatcher.match(image0, image1);
    }

    private void checkFileExists(Path path) {
        if (!path.toFile().isFile()) {
            throw new RuntimeException("File " + path.getFileName() + " not found.");
        }
    }

    private void checkParentDirectoryExists(Path path) {
        if (!path.toAbsolutePath().getParent().toFile().isDirectory()) {
            throw new RuntimeException("Directory " + path.getFileName() + " not found.");
        }
    }

    private Image snapshotNodeToImage(Node node) {
        return node.snapshot(null, null);
    }

    private Image readImageFromStream(InputStream inputStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void writeImageToStream(Image image,
                                    String imageFormat,
                                    OutputStream outputStream) throws IOException {
        BufferedImage imageWithType = new BufferedImage((int) image.getWidth(),
                (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, imageWithType);
        ImageIO.write(bufferedImage, imageFormat, outputStream);
        if (!ImageIO.write(bufferedImage, imageFormat, outputStream)) {
            throw new IOException("Image was not created");
        }
    }

    private Image blendImages(Image image0,
                              Image image1,
                              BlendMode blendMode,
                              Pos alignment) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(alignment);
        stackPane.setBlendMode(blendMode);
        stackPane.getChildren().add(new ImageView(image0));
        stackPane.getChildren().add(new ImageView(image1));
        return captureNode(stackPane);
    }
}
