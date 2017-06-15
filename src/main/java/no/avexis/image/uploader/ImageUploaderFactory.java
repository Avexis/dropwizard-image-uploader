package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class ImageUploaderFactory {

    @JsonProperty
    @NotEmpty
    private String directory;
    @JsonProperty
    private String filenameFormat;
    @JsonProperty
    private ImageFileType forcedFileType;
    @JsonProperty
    private List<ImageFileType> allowedFileTypes;
    @JsonProperty
    @NotEmpty
    private List<ImageSize> resolutions;

    public ImageUploaderFactory() {
        this.directory = "";
        this.filenameFormat = "%1$s_%2$s_%3$s.%6$s";
        this.forcedFileType = null;
        this.allowedFileTypes = new ArrayList<>();
        this.resolutions = new ArrayList<>();
    }

    private String createFilename(final String filename, final ImageSize size, final String extension) {
        return String.format(this.filenameFormat, filename, size.getWidth(), size.getHeight(), size.getName(), size.isCrop() ? "crop" : "", extension);
    }

    @ValidationMethod(message = "Either forcedFileType or allowedFileTypes must contain some value")
    @JsonIgnore
    public boolean fileTypeSpecified() {
        return containsForcedFileType() || containsAllowedFileTypes();
    }

    @ValidationMethod(message = "Can not define both forcedFileType and allowedFileTypes")
    @JsonIgnore
    public boolean doesNotContainBothForcedAndAllowed() {
        return !(containsForcedFileType() && containsAllowedFileTypes());
    }

    @JsonIgnore
    private boolean containsForcedFileType() {
        return forcedFileType != null;
    }

    @JsonIgnore
    private boolean containsAllowedFileTypes() {
        return allowedFileTypes != null && !allowedFileTypes.isEmpty();
    }
}
