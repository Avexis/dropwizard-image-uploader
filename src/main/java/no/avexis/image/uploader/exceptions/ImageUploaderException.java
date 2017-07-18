package no.avexis.image.uploader.exceptions;

public class ImageUploaderException extends Exception {
    public ImageUploaderException(String message) {
        super(message);
    }

    public ImageUploaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
