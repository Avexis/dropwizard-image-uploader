package no.avexis.image.uploader.storers;

import no.avexis.image.uploader.exceptions.ImageUploaderException;

import java.awt.image.BufferedImage;
import java.util.UUID;

public interface AbstractImageStorer {

    void save(final UUID imageId, final BufferedImage bufferedImage, final String filename) throws ImageUploaderException;
}
