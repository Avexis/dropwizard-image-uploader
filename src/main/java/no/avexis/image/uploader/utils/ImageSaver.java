package no.avexis.image.uploader.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSaver {
    private ImageSaver() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean save(final String directory, final BufferedImage bufferedImage, final String filename) throws IOException {
        return save(directory, bufferedImage, filename);
    }

    public static boolean save(final String directory, final BufferedImage bufferedImage, final String filename, final String extension) throws IOException {
        final File imageDirectory = new File(directory);
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new IOException("Could not create folder: " + imageDirectory.getAbsolutePath());
        }
        final File imageFile = new File(directory, filename);
        return ImageIO.write(bufferedImage, extension, imageFile);
    }
}
