package no.avexis.image.uploader.transformers;

import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ImageSize;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.awt.image.BufferedImage;

public abstract class AbstractImageTransformer {

    private final BufferedImage bufferedImage;
    private final FormDataContentDisposition formDataContentDisposition;

    public AbstractImageTransformer(final BufferedImage bufferedImage, final FormDataContentDisposition formDataContentDisposition) {
        this.bufferedImage = bufferedImage;
        this.formDataContentDisposition = formDataContentDisposition;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public FormDataContentDisposition getFormDataContentDisposition() {
        return formDataContentDisposition;
    }

    public abstract BufferedImage toBufferedImage(final ImageSize imageSize) throws ImageUploaderException;

    public abstract String toBase64(final BufferedImage bufferedImage) throws ImageUploaderException;
}
