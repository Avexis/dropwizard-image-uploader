package no.avexis.image.uploader.utils;

import no.avexis.image.uploader.exceptions.ImageUploaderException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSaver {
    private ImageSaver() {
        throw new IllegalStateException("Utility class");
    }

    public static void save(final String directory, final BufferedImage bufferedImage, final String filename) throws ImageUploaderException {
        save(directory, bufferedImage, filename);
    }

    public static void save(final String directory, final BufferedImage bufferedImage, final String filename, final String extension) throws ImageUploaderException {
        final File imageDirectory = new File(directory);
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new ImageUploaderException("Could not create folder: " + imageDirectory.getAbsolutePath());
        }
        final File imageFile = new File(directory, filename);
        try {
            ImageIO.write(bufferedImage, extension, imageFile);
        } catch (IOException e) {
            throw new ImageUploaderException("Could not save image", e);
        }
    }
}
