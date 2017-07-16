package no.avexis.image.uploader;

import no.avexis.image.uploader.models.Image;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.InputStream;
import java.util.List;

public class ImageBuilder {

    private String directory;
    private String filenameFormat;
    private ImageFileType forcedFileType;
    private List<ImageFileType> allowedFileTypes;
    private List<ImageSize> resolutions;

    public ImageBuilder(String directory, String filenameFormat, ImageFileType forcedFileType, List<ImageFileType> allowedFileTypes, List<ImageSize> resolutions) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.forcedFileType = forcedFileType;
        this.allowedFileTypes = allowedFileTypes;
        this.resolutions = resolutions;
    }

    public Image save(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) {

        return null;
    }
}
