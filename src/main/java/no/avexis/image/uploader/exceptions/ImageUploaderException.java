package no.avexis.image.uploader.exceptions;

public class ImageUploaderException extends Exception {
    public ImageUploaderException(final String message) {
        super(message);
    }

    public ImageUploaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
