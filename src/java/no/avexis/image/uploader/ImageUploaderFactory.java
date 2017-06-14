package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ImageUploaderFactory {

    @JsonProperty
    private String directory;
    @JsonProperty
    private String filenameFormat;
    @JsonProperty
    private String forcedFormat;
    @JsonProperty
    private List<ImageWrapper> resolutions;

    public ImageUploaderFactory() {
        this.directory = directory;
        this.filenameFormat = "%s_%d_%d.%s";
        this.forcedFormat = forcedFormat;
        this.resolutions = resolutions;
    }

    public ImageUploaderFactory(final String directory, final String filenameFormat, final String forcedFormat, final List<ImageWrapper> resolutions) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.forcedFormat = forcedFormat;
        this.resolutions = resolutions;
    }

    private String createWrapperFilename(final ImageWrapper wrapper, final String filename, final String extension) {
        return String.format(this.filenameFormat, filename, wrapper.getWidth(), wrapper.getHeight(), extension);
    }
}
