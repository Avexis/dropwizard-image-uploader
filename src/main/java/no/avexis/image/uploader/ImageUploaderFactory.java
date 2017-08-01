package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import no.avexis.image.uploader.exceptions.ImageUploaderDirectoryMissingException;
import no.avexis.image.uploader.models.ImageFileFormat;
import no.avexis.image.uploader.models.ImageSize;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUploaderFactory {

    @JsonProperty
    @NotEmpty
    private String directory;
    @JsonProperty
    private boolean createDirectory;
    @JsonProperty
    private String filenameFormat;
    @JsonProperty
    private List<ImageFileFormat> allowedFileFormats;
    @JsonProperty
    @NotEmpty
    private List<ImageSize> imageSizes;

    public ImageUploaderFactory() {
        this.directory = "";
        this.filenameFormat = "%1$s_%2$s_%3$s.%6$s";
        this.allowedFileFormats = new ArrayList<>();
        this.imageSizes = new ArrayList<>();
    }

    @JsonIgnore
    private boolean containsAllowedFileTypes() {
        return allowedFileFormats != null && !allowedFileFormats.isEmpty();
    }

    public ImageBuilder createBuilder() {
        initDirectory();
        return new ImageBuilder(directory, filenameFormat, allowedFileFormats, imageSizes);
    }

    private void initDirectory() {
        final File dir = new File(directory);
        if (!dir.exists() && !createDirectory) {
            throw new ImageUploaderDirectoryMissingException(String.format("Directory '%1$s' does not exist", directory));
        } else if (!dir.isDirectory()) {
            throw new ImageUploaderDirectoryMissingException(String.format("'%1$s' is not a directory", directory));
        } else if (!dir.canWrite()) {
            throw new ImageUploaderDirectoryMissingException(String.format("Can not write to directory '%1$s', insufficient permissions?", directory));
        }
        if (!dir.mkdirs()) {
            throw new ImageUploaderDirectoryMissingException(String.format("Could not create directory '%1$s'", directory));
        }
    }
}
