package no.avexis.image.saver;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import no.avexis.image.saver.models.ResolutionTemplate;
import no.avexis.image.saver.savers.AbstractImageSaver;
import no.avexis.image.saver.transformers.AbstractImageTransformer;
import no.avexis.image.saver.transformers.BasicImageTransformer;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageSaverFactory {

    @JsonProperty
    private final String filenameFormat;
    @JsonProperty
    private final AbstractImageSaver imageSaver;
    @JsonProperty
    @NotEmpty
    private final List<ResolutionTemplate> templates;
    private Map<String, AbstractImageTransformer> imageTransformers;

    public ImageSaverFactory(final AbstractImageSaver imageSaver,
                             final List<ResolutionTemplate> templates) {
        this("%1$s_%4$s_%2$s_%3$s.%5$s", imageSaver, templates);
    }

    public ImageSaverFactory(final String filenameFormat,
                             final AbstractImageSaver imageSaver,
                             final List<ResolutionTemplate> templates) {
        this.filenameFormat = filenameFormat;
        this.imageSaver = imageSaver;
        this.templates = templates;
        this.imageTransformers = basicImageTransformers();
    }

    public ImageSaverController build() {
        return new ImageSaverController(imageSaver, filenameFormat, imageTransformers, templates);
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
