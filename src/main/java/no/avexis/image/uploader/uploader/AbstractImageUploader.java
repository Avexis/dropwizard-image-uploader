package no.avexis.image.uploader.uploader;

import no.avexis.image.uploader.exceptions.ImageUploaderException;

import java.awt.image.BufferedImage;
import java.util.UUID;

public interface AbstractImageUploader {

    void save(final UUID imageId, final BufferedImage bufferedImage, final String filename) throws ImageUploaderException;
}
