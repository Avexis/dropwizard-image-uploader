package no.avexis.image.storer;

import com.google.common.io.Files;
import no.avexis.image.storer.exceptions.ImageStorerException;
import no.avexis.image.storer.models.Image;
import no.avexis.image.storer.models.Resolution;
import no.avexis.image.storer.models.ResolutionTemplate;
import no.avexis.image.storer.transformers.AbstractImageTransformer;
import no.avexis.image.storer.transformers.BasicImageTransformer;
import no.avexis.image.storer.utils.ImageStorer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ImageBuilder {

    private final String directory;
    private final String filenameFormat;
    private final Map<String, AbstractImageTransformer> imageTransformers;
    private final List<ResolutionTemplate> templates;

    public ImageBuilder(final String directory, final String filenameFormat, final Map<String, AbstractImageTransformer> imageTransformers, final List<ResolutionTemplate> templates) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.imageTransformers = imageTransformers;
        this.templates = templates;
    }

    public Image save(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageStorerException {
        final String extension = getExtension(formDataContentDisposition);
        Image image = new Image();
        final BufferedImage bufferedImage = readInputStream(inputStream);
        final AbstractImageTransformer transformer = getTransformer(extension);
        for (ResolutionTemplate template : templates) {
            final BufferedImage transformedImage = transformer.toBufferedImage(bufferedImage, template);
            final Resolution resolution = createResolution(template, transformedImage);
            if (template.isBase64()) {
                resolution.setFile(transformer.toBase64(transformedImage, extension));
            } else {
                final String filename = createFilename(resolution, template, formDataContentDisposition);
                ImageStorer.save(directory + "/" + image.getId(), transformedImage, filename);
                resolution.setFile(filename);
            }
            image.getResolutions().put(template.getName(), resolution);
        }
        return image;
    }

    private AbstractImageTransformer getTransformer(final String fileType) {
        final AbstractImageTransformer transformer = imageTransformers.get(fileType.toUpperCase());
        if (null == transformer) {
            throw new NullPointerException("No ImageTransformer for fileType " + fileType);
        }
        return transformer;
    }

    private Resolution createResolution(final ResolutionTemplate template, final BufferedImage bufferedImage) {
        Resolution resolution = new Resolution();
        resolution.setWidth(bufferedImage.getWidth());
        resolution.setHeight(bufferedImage.getHeight());
        resolution.setBase64(template.isBase64());
        return resolution;
    }

    private static BufferedImage readInputStream(final InputStream inputStream) throws ImageStorerException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new ImageStorerException("Could not read image InputStream", e);
        }
    }

    private static String getExtension(final FormDataContentDisposition formDataContentDisposition) {
        return Files.getFileExtension(formDataContentDisposition.getFileName());
    }

    private String createFilename(final Resolution resolution, final ResolutionTemplate template, final FormDataContentDisposition formDataContentDisposition) {
        final String originalFilename = formDataContentDisposition.getFileName();
        return createFilename(resolution, template, Files.getNameWithoutExtension(originalFilename), Files.getFileExtension(originalFilename));
    }

    private String createFilename(final Resolution resolution, final ResolutionTemplate template, final String filename, final String extension) {
        return String.format(this.filenameFormat, filename, resolution.getWidth(), resolution.getHeight(), template.getName(), template.isCrop() ? "crop" : "", extension);
    }
}
