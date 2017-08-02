package no.avexis.image.uploader.transformers;

import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.awt.image.BufferedImage;

public abstract class AbstractImageTransformer {

    public abstract BufferedImage toBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageUploaderException;

    public abstract String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageUploaderException;
}
