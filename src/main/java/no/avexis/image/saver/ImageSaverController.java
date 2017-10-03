package no.avexis.image.saver;

import com.google.common.io.Files;
import no.avexis.image.saver.exceptions.ImageSaverException;
import no.avexis.image.saver.models.Image;
import no.avexis.image.saver.models.Resolution;
import no.avexis.image.saver.models.ResolutionTemplate;
import no.avexis.image.saver.savers.AbstractImageSaver;
import no.avexis.image.saver.transformers.AbstractImageTransformer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ImageSaverController {

    private final Map<String, AbstractImageTransformer> imageTransformers;
    private AbstractImageSaver imageSaver;
    private ResolutionCreator resolutionCreator;

    public ImageSaverController(final AbstractImageSaver imageSaver, final String filenameFormat, final Map<String, AbstractImageTransformer> imageTransformers, final List<ResolutionTemplate> templates) {
        this.imageSaver = imageSaver;
        this.imageTransformers = imageTransformers;
        this.resolutionCreator = new ResolutionCreator(filenameFormat, templates);
    }

    public Image store(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageSaverException {
        return store(inputStream, formDataContentDisposition.getFileName());
    }

    public Image store(final InputStream inputStream, final String filename) throws ImageSaverException {
        final String extension = Files.getFileExtension(filename);
        Image image = new Image();
        final BufferedImage bufferedImage = readInputStream(inputStream);
        final AbstractImageTransformer transformer = getTransformer(extension);
        image.setResolutions(createAndSaveResolutions(image.getId(), bufferedImage, filename, extension, transformer));
        return image;
    }

    private Map<String, Resolution> createAndSaveResolutions(final UUID imageId, final BufferedImage bufferedImage, final String filename, final String extension, final AbstractImageTransformer transformer) throws ImageSaverException {
        final Map<String, Map.Entry<Resolution, BufferedImage>> resolutionsUnsaved = resolutionCreator.createResolutions(bufferedImage, filename, extension, transformer);
        final Map<String, Resolution> resolutions = new HashMap<>();
        for (final Map.Entry<String, Map.Entry<Resolution, BufferedImage>> entry : resolutionsUnsaved.entrySet()) {
            final String name = entry.getKey();
            final Resolution resolution = entry.getValue().getKey();
            final BufferedImage buffImg = entry.getValue().getValue();
            if (!resolution.isBase64()) {
                imageSaver.save(imageId, buffImg, resolution.getFile());
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

    private static BufferedImage readInputStream(final InputStream inputStream) throws ImageSaverException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new ImageSaverException("Could not read image InputStream", e);
        }
    }
}
