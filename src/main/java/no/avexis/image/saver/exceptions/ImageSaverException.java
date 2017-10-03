package no.avexis.image.saver.exceptions;

public class ImageSaverException extends Exception {
    public ImageSaverException(final String message) {
        super(message);
    }

    public ImageSaverException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
