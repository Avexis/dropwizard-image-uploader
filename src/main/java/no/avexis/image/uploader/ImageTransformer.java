package no.avexis.image.uploader;

import no.avexis.image.uploader.models.Resolution;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ImageTransformer {
    private InputStream inputStream;
    private FormDataContentDisposition formDataContentDisposition;

    public ImageTransformer(InputStream inputStream, FormDataContentDisposition formDataContentDisposition) {
        this.inputStream = inputStream;
        this.formDataContentDisposition = formDataContentDisposition;
    }

    //TODO rename method to more appropriate name
    public Resolution calculateResolution(final ImageSize size) {
        final Resolution resolution = new Resolution();
        resolution.setBase64(size.isBase64());
        return null;
    }

    public BufferedImage toBufferedImage(final Resolution resolution) {
        return null;
    }

    public String toBase64(final Resolution resolution) {
        return null;
    }
}
