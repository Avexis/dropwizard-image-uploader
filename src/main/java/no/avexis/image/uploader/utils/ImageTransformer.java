package no.avexis.image.uploader.utils;

import no.avexis.image.uploader.models.ImageFileFormat;
import no.avexis.image.uploader.models.ImageSize;
import no.avexis.image.uploader.models.Resolution;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageTransformer {
    private BufferedImage bufferedImage;
    private FormDataContentDisposition formDataContentDisposition;

    public ImageTransformer(final BufferedImage bufferedImage, final FormDataContentDisposition formDataContentDisposition) {
        this.bufferedImage = bufferedImage;
        this.formDataContentDisposition = formDataContentDisposition;
    }

    public Resolution createResolution(final ImageSize size) {
        final Resolution resolution = new Resolution();
        resolution.setBase64(size.isBase64());
        final int originalWidth = bufferedImage.getWidth();
        return null;
    }

    public BufferedImage toBufferedImage(final Resolution resolution) {
        return null;
    }

    public String toBase64(final Resolution resolution) {
        return null;
    }

    public void forceFileFormat(ImageFileFormat forcedFileFormat) {
    }
}
