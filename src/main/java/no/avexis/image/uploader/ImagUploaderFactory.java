package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import no.avexis.image.uploader.models.ResolutionTemplate;
import no.avexis.image.uploader.storers.AbstractImageStorer;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;
import no.avexis.image.uploader.transformers.BasicImageTransformer;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagUploaderFactory {

    @JsonProperty
    private final String filenameFormat;
    @JsonProperty
    private final AbstractImageStorer imageUploader;
    @JsonProperty
    @NotEmpty
    private final List<ResolutionTemplate> templates;
    private Map<String, AbstractImageTransformer> imageTransformers;

    public ImagUploaderFactory(final AbstractImageStorer imageUploader,
                               final List<ResolutionTemplate> templates) {
        this("%1$s_%4$s_%2$s_%3$s.%5$s", imageUploader, templates);
    }

    public ImagUploaderFactory(final String filenameFormat,
                               final AbstractImageStorer imageUploader,
                               final List<ResolutionTemplate> templates) {
        this.filenameFormat = filenameFormat;
        this.imageUploader = imageUploader;
        this.templates = templates;
        this.imageTransformers = basicImageTransformers();
    }

    public ImageUploader build() {
        return new ImageUploader(imageUploader, filenameFormat, imageTransformers, templates);
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

    public String getFilenameFormat() {
        return filenameFormat;
    }

    public List<ResolutionTemplate> getTemplates() {
        return templates;
    }
}
