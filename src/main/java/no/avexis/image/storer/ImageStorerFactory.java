package no.avexis.image.storer;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import no.avexis.image.storer.exceptions.ImageStorerDirectoryMissingException;
import no.avexis.image.storer.models.ResolutionTemplate;
import no.avexis.image.storer.transformers.AbstractImageTransformer;
import no.avexis.image.storer.transformers.BasicImageTransformer;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageStorerFactory {

    @JsonProperty
    @NotEmpty
    private final String directory;
    @JsonProperty
    private boolean createDirectory;
    @JsonProperty
    private final String filenameFormat;
    @JsonProperty
    @NotEmpty
    private final List<ResolutionTemplate> templates;
    private Map<String, AbstractImageTransformer> imageTransformers;
    @JsonProperty
    private boolean runParallel;

    public ImageStorerFactory(final String directory, final List<ResolutionTemplate> templates) {
        this(directory, false, templates);
    }

    public ImageStorerFactory(final String directory, final boolean createDirectory, final List<ResolutionTemplate> templates) {
        this(directory, createDirectory, "%1$s_%4$s_%2$s_%3$s.%5$s", templates, false);
    }

    public ImageStorerFactory(final String directory, final boolean createDirectory, final String filenameFormat,
                              final List<ResolutionTemplate> templates, final boolean runParallel) {
        this.directory = directory;
        this.createDirectory = createDirectory;
        this.filenameFormat = filenameFormat;
        this.templates = templates;
        this.imageTransformers = basicImageTransformers();
        this.runParallel = runParallel;
    }


    public ImageStorer create() {
        initDirectory();
        return new ImageStorer(directory, filenameFormat, imageTransformers, templates, runParallel);
    }

    private void initDirectory() {
        final File dir = new File(directory);
        if (!dir.exists()) {
            if (!createDirectory) {
                throw new ImageStorerDirectoryMissingException(String.format("Directory '%1$s' does not exist", directory));
            }
            if (!dir.mkdirs()) {
                throw new ImageStorerDirectoryMissingException(String.format("Could not create directory '%1$s'", directory));
            }
        }
        if (!dir.isDirectory()) {
            throw new ImageStorerDirectoryMissingException(String.format("'%1$s' is not a directory", directory));
        }
        if (!dir.canRead()) {
            throw new ImageStorerDirectoryMissingException(String.format("Can not read from directory '%1$s', insufficient permissions?", directory));
        }
        if (!dir.canWrite()) {
            throw new ImageStorerDirectoryMissingException(String.format("Can not write to directory '%1$s', insufficient permissions?", directory));
        }
    }

    private Map<String, AbstractImageTransformer> basicImageTransformers() {
        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        for (final String format : ThumbnailatorUtils.getSupportedOutputFormats()) {
            transformers.put(format.toUpperCase(), new BasicImageTransformer());
        }
        return transformers;
    }

    public void addImageTransformer(final String format, final AbstractImageTransformer imageTransformer) {
        this.imageTransformers.put(format.toUpperCase(), imageTransformer);
    }

    public boolean removeImageTransformer(final String format) {
        return this.imageTransformers.remove(format) != null;
    }

    public boolean imageTransformerExists(final String format) {
        return this.imageTransformers.containsKey(format);
    }

    public String getDirectory() {
        return directory;
    }

    public boolean isCreateDirectory() {
        return createDirectory;
    }

    public String getFilenameFormat() {
        return filenameFormat;
    }

    public List<ResolutionTemplate> getTemplates() {
        return templates;
    }
}
