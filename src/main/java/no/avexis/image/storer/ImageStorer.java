package no.avexis.image.storer;

import com.google.common.io.Files;
import no.avexis.image.storer.exceptions.ImageStorerException;
import no.avexis.image.storer.models.Image;
import no.avexis.image.storer.models.Resolution;
import no.avexis.image.storer.models.ResolutionTemplate;
import no.avexis.image.storer.transformers.AbstractImageTransformer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ImageStorer {

    private final String directory;
    private final String filenameFormat;
    private final Map<String, AbstractImageTransformer> imageTransformers;
    private final List<ResolutionTemplate> templates;
    private final boolean runParallel;

    public ImageStorer(final String directory, final String filenameFormat, final Map<String, AbstractImageTransformer> imageTransformers, final List<ResolutionTemplate> templates, final boolean runParallel) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.imageTransformers = imageTransformers;
        this.templates = templates;
        this.runParallel = runParallel;
    }

    public Image store(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageStorerException {
        return store(inputStream, formDataContentDisposition.getFileName());
    }

    public Image store(final InputStream inputStream, final String filename) throws ImageStorerException {
        final String extension = Files.getFileExtension(filename);
        Image image = new Image();
        final BufferedImage bufferedImage = readInputStream(inputStream);
        final AbstractImageTransformer transformer = getTransformer(extension);
        final String absoluteFolderPath = String.format("%s/%s", directory, image.getId());
        if (runParallel) {
            image.setResolutions(createAndStoreResolutionsThreaded(bufferedImage, filename, extension, transformer, absoluteFolderPath));

        } else {
            image.setResolutions(createAndStoreResolutionsUnthreaded(bufferedImage, filename, extension, transformer, absoluteFolderPath));
        }
        return image;
    }

    private Map<String, Resolution> createAndStoreResolutionsThreaded(final BufferedImage bufferedImage, final String filename, final String extension,
                                                                      final AbstractImageTransformer transformer, final String folder) throws ImageStorerException {
        final Map<String, Resolution> resolutions = new ConcurrentHashMap<>();
        CompletionService<Map.Entry<String, Resolution>> compService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(4));
        int remainingFutures = 0;

        for (ResolutionTemplate template : templates) {
            remainingFutures++;
            compService.submit(() -> new AbstractMap.SimpleEntry<>(template.getName(), createAndStoreResolution(bufferedImage, filename, extension, transformer, folder, template)));
        }
        Future<Map.Entry<String, Resolution>> completedFuture;
        Map.Entry<String, Resolution> res;
        try {
            while (remainingFutures > 0) {
                completedFuture = compService.take();
                remainingFutures--;
                res = completedFuture.get();
                resolutions.put(res.getKey(), res.getValue());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ImageStorerException("A Future failed to execute", e);
        }
        return resolutions;
    }

    private Map<String, Resolution> createAndStoreResolutionsUnthreaded(final BufferedImage bufferedImage, final String filename, final String extension,
                                                                        final AbstractImageTransformer transformer, final String folder) throws ImageStorerException {
        final Map<String, Resolution> resolutions = new HashMap<>();
        for (ResolutionTemplate template : templates) {
            resolutions.put(template.getName(), createAndStoreResolution(bufferedImage, filename, extension, transformer, folder, template));
        }
        return resolutions;
    }

    private Resolution createAndStoreResolution(final BufferedImage bufferedImage, final String filename, final String extension,
                                                final AbstractImageTransformer transformer, final String absoluteFolderPath, final ResolutionTemplate template) throws ImageStorerException {
        final BufferedImage transformedImage = transformer.resizeBufferedImage(bufferedImage, template);
        final Resolution resolution = createResolution(template, transformedImage);
        if (template.isBase64()) {
            resolution.setFile(transformer.toBase64(transformedImage, extension));
        } else {
            final String formattedFilename = createFilename(resolution, template.getName(), filename);
            save(absoluteFolderPath, transformedImage, formattedFilename);
            resolution.setFile(formattedFilename);
        }
        return resolution;
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

    private String createFilename(final Resolution resolution, final String templateName, final String filename) {
        final String nameWithoutExtension = Files.getNameWithoutExtension(filename);
        final String extension = Files.getFileExtension(filename);
        return String.format(this.filenameFormat, nameWithoutExtension, resolution.getWidth(), resolution.getHeight(), templateName, extension);
    }

    private void save(final String directory, final BufferedImage bufferedImage, final String filename) throws ImageStorerException {
        final String extension = Files.getFileExtension(filename);
        final File imageDirectory = new File(directory);
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new ImageStorerException("Could not create folder: " + imageDirectory.getAbsolutePath());
        }
        final File imageFile = new File(directory, filename);
        try {
            ImageIO.write(bufferedImage, extension, imageFile);
        } catch (IOException e) {
            throw new ImageStorerException("Could not save image", e);
        }
    }
}
