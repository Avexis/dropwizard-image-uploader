package no.avexis.image.storer.exceptions;

public class ImageStorerException extends Exception {
    public ImageStorerException(final String message) {
        super(message);
    }

    public ImageStorerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
