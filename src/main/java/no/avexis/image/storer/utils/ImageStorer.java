package no.avexis.image.storer.utils;

import no.avexis.image.storer.exceptions.ImageStorerException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageStorer {
    private ImageStorer() {
        throw new IllegalStateException("Utility class");
    }

    public static void save(final String directory, final BufferedImage bufferedImage, final String filename) throws ImageStorerException {
        save(directory, bufferedImage, filename);
    }

    public static void save(final String directory, final BufferedImage bufferedImage, final String filename, final String extension) throws ImageStorerException {
        final File imageDirectory = new File(directory);
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new ImageStorerException("Could not create folder: " + imageDirectory.getAbsolutePath());
        }
        final File imageFile = new File(directory, filename);
        try {
            ImageIO.write(bufferedImage, extension, imageFile);
        } catch (IOException e) {
            throw new ImageStorerException("Could not save image", e);
        }
    }
}
