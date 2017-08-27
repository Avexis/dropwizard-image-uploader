package no.avexis.image.storer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.avexis.image.storer.exceptions.ImageStorerDirectoryMissingException;
import no.avexis.image.storer.models.ImageFileFormat;
import no.avexis.image.storer.models.ResolutionTemplate;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageStorerFactory {

    @JsonProperty
    @NotEmpty
    private final String directory;
    @JsonProperty
    private boolean createDirectory;
    @JsonProperty
    private final String filenameFormat;
    @JsonProperty
    private final List<ImageFileFormat> allowedFileFormats;
    @JsonProperty
    @NotEmpty
    private final List<ResolutionTemplate> templates;

    public ImageStorerFactory() {
        this.directory = "";
        this.filenameFormat = "%1$s_%2$s_%3$s.%6$s";
        this.allowedFileFormats = new ArrayList<>();
        this.templates = new ArrayList<>();
    }

    @JsonIgnore
    private boolean containsAllowedFileTypes() {
        return allowedFileFormats != null && !allowedFileFormats.isEmpty();
    }

    public ImageBuilder createBuilder() {
        initDirectory();
        return new ImageBuilder(directory, filenameFormat, allowedFileFormats, templates);
    }

    private void initDirectory() {
        final File dir = new File(directory);
        if (!dir.exists() && !createDirectory) {
            throw new ImageStorerDirectoryMissingException(String.format("Directory '%1$s' does not exist", directory));
        } else if (!dir.isDirectory()) {
            throw new ImageStorerDirectoryMissingException(String.format("'%1$s' is not a directory", directory));
        } else if (!dir.canWrite()) {
            throw new ImageStorerDirectoryMissingException(String.format("Can not write to directory '%1$s', insufficient permissions?", directory));
        }
        if (!dir.mkdirs()) {
            throw new ImageStorerDirectoryMissingException(String.format("Could not create directory '%1$s'", directory));
        }
    }
}
