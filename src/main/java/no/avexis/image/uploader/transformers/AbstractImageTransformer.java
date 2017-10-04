package no.avexis.image.uploader.transformers;

import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;

import java.awt.image.BufferedImage;

public interface AbstractImageTransformer {

    BufferedImage resizeBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageUploaderException;

    String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageUploaderException;
}
