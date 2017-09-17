package no.avexis.image.storer.exceptions;

public class ImageStorerException extends Exception {
    public ImageStorerException(String message) {
        super(message);
    }

    public ImageStorerException(Throwable cause) {
        super(cause);
    }

    public ImageStorerException(String message, Throwable cause) {
        super(message, cause);
    }
}
