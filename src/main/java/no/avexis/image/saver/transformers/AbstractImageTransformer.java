package no.avexis.image.saver.transformers;

import no.avexis.image.saver.exceptions.ImageSaverException;
import no.avexis.image.saver.models.ResolutionTemplate;

import java.awt.image.BufferedImage;

public interface AbstractImageTransformer {

    BufferedImage resizeBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageSaverException;

    String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageSaverException;
}
