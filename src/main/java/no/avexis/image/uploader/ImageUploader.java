package no.avexis.image.uploader;

import com.google.common.io.Files;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.Image;
import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.models.ResolutionTemplate;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;
import no.avexis.image.uploader.uploader.AbstractImageUploader;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageUploader {

    private final Map<String, AbstractImageTransformer> imageTransformers;
    private AbstractImageUploader imageUploader;
    private ResolutionCreator resolutionCreator;

    public ImageUploader(final AbstractImageUploader imageUploader, final String filenameFormat, final Map<String, AbstractImageTransformer> imageTransformers, final List<ResolutionTemplate> templates) {
        this.imageUploader = imageUploader;
        this.imageTransformers = imageTransformers;
        this.resolutionCreator = new ResolutionCreator(filenameFormat, templates);
    }

    public Image store(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        return store(inputStream, formDataContentDisposition.getFileName());
    }

    public Image store(final InputStream inputStream, final String filename) throws ImageUploaderException {
        final String extension = Files.getFileExtension(filename);
        Image image = new Image();
        final BufferedImage bufferedImage = readInputStream(inputStream);
        final AbstractImageTransformer transformer = getTransformer(extension);
        final Map<String, Resolution> resolutions = createAndSaveResolutions(image.getId(), bufferedImage, filename, transformer);
        image.setResolutions(resolutions);
        return image;
    }

    private Map<String, Resolution> createAndSaveResolutions(final UUID imageId, final BufferedImage bufferedImage, final String filename, final AbstractImageTransformer transformer) throws ImageUploaderException {
        final Map<String, Map.Entry<Resolution, BufferedImage>> resolutionsUnsaved = resolutionCreator.createResolutions(bufferedImage, filename, transformer);
        final Map<String, Resolution> resolutions = new HashMap<>();
        for (final Map.Entry<String, Map.Entry<Resolution, BufferedImage>> entry : resolutionsUnsaved.entrySet()) {
            final String name = entry.getKey();
            final Resolution resolution = entry.getValue().getKey();
            final BufferedImage buffImg = entry.getValue().getValue();
            if (!resolution.isBase64()) {
                imageUploader.save(imageId, buffImg, resolution.getFile());
            }
            resolutions.put(name, resolution);
        }
        return resolutions;
    }

    private AbstractImageTransformer getTransformer(final String fileType) {
        final AbstractImageTransformer transformer = imageTransformers.get(fileType.toUpperCase());
        if (null == transformer) {
            throw new NullPointerException("No ImageTransformer for fileType " + fileType);
        }
        return transformer;
    }

    private static BufferedImage readInputStream(final InputStream inputStream) throws ImageUploaderException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new ImageUploaderException("Could not read image InputStream", e);
        }
    }
}
