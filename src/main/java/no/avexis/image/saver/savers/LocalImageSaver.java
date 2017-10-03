package no.avexis.image.saver.savers;

import com.google.common.io.Files;
import no.avexis.image.saver.exceptions.ImageSaverDirectoryMissingException;
import no.avexis.image.saver.exceptions.ImageSaverException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LocalImageSaver implements AbstractImageSaver {

    private String directory;
    private Boolean createDirectory;

    public LocalImageSaver(final String directory, final Boolean createDirectory) {
        this.directory = directory;
        this.createDirectory = createDirectory;
        validateDirectory();
    }

    @Override
    public void save(final UUID imageId, final BufferedImage bufferedImage, final String filename) throws ImageSaverException {
        final String absolutePath = String.format("%1$s/%2$s", directory, imageId);
        final String extension = Files.getFileExtension(filename);
        final File imageDirectory = new File(absolutePath);
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new ImageSaverException("Could not build folder: " + imageDirectory.getAbsolutePath());
        }
        final File imageFile = new File(absolutePath, filename);
        try {
            ImageIO.write(bufferedImage, extension, imageFile);
        } catch (IOException e) {
            throw new ImageSaverException("Could not save image", e);
        }
    }

    private void validateDirectory() {
        final File dir = new File(directory);
        if (!dir.exists()) {
            if (!createDirectory) {
                throw new ImageSaverDirectoryMissingException(String.format("Directory '%1$s' does not exist", directory));
            }
            if (!dir.mkdirs()) {
                throw new ImageSaverDirectoryMissingException(String.format("Could not build directory '%1$s'", directory));
            }
        }
        if (!dir.isDirectory()) {
            throw new ImageSaverDirectoryMissingException(String.format("'%1$s' is not a directory", directory));
        }
        if (!dir.canRead()) {
            throw new ImageSaverDirectoryMissingException(String.format("Can not read from directory '%1$s', insufficient permissions?", directory));
        }
        if (!dir.canWrite()) {
            throw new ImageSaverDirectoryMissingException(String.format("Can not write to directory '%1$s', insufficient permissions?", directory));
        }
    }
}
