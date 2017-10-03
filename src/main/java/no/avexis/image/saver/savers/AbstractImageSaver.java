package no.avexis.image.saver.savers;

import no.avexis.image.saver.exceptions.ImageSaverException;

import java.awt.image.BufferedImage;
import java.util.UUID;

public interface AbstractImageSaver {

    void save(final UUID imageId, final BufferedImage bufferedImage, final String filename) throws ImageSaverException;
}
