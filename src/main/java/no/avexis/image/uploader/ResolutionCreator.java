package no.avexis.image.uploader;

import com.google.common.io.Files;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.models.ResolutionTemplate;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;

import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ResolutionCreator {

    private final String filenameFormat;
    private final List<ResolutionTemplate> templates;

    public ResolutionCreator(final String filenameFormat, final List<ResolutionTemplate> templates) {
        this.filenameFormat = filenameFormat;
        this.templates = templates;
    }

    public Map<String, Map.Entry<Resolution, BufferedImage>> createResolutions(final BufferedImage bufferedImage, final String filename,
                                                                                final AbstractImageTransformer transformer) throws ImageUploaderException {
        final Map<String, Map.Entry<Resolution, BufferedImage>> resolutions = new ConcurrentHashMap<>();
        CompletionService<Map.Entry<String, Map.Entry<Resolution, BufferedImage>>> compService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(4));
        int remainingFutures = 0;

        for (ResolutionTemplate template : templates) {
            remainingFutures++;
            compService.submit(() -> new AbstractMap.SimpleEntry<>(template.getName(), createResolutionAndBufferedImage(bufferedImage, filename, transformer, template)));
        }
        Future<Map.Entry<String, Map.Entry<Resolution, BufferedImage>>> completedFuture;
        Map.Entry<String, Map.Entry<Resolution, BufferedImage>> res;
        try {
            while (remainingFutures > 0) {
                completedFuture = compService.take();
                remainingFutures--;
                res = completedFuture.get();
                resolutions.put(res.getKey(), res.getValue());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ImageUploaderException("A Future failed to execute", e);
        }
        return resolutions;
    }

    private Map.Entry<Resolution, BufferedImage> createResolutionAndBufferedImage(final BufferedImage bufferedImage, final String filename,
                                                                  final AbstractImageTransformer transformer, final ResolutionTemplate template) throws ImageUploaderException {
        final BufferedImage transformedImage = transformer.resizeBufferedImage(bufferedImage, template);
        final Resolution resolution = createResolution(template, transformedImage);
        if (template.isBase64()) {
            resolution.setFile(transformer.toBase64(transformedImage, Files.getFileExtension(filename)));
            return new AbstractMap.SimpleEntry<>(resolution, null);
        } else {
            resolution.setFile(createFilename(resolution, template.getName(), filename));
            return new AbstractMap.SimpleEntry<>(resolution, transformedImage);
        }
    }

    private Resolution createResolution(final ResolutionTemplate template, final BufferedImage bufferedImage) {
        Resolution resolution = new Resolution();
        resolution.setWidth(bufferedImage.getWidth());
        resolution.setHeight(bufferedImage.getHeight());
        resolution.setBase64(template.isBase64());
        return resolution;
    }

    private String createFilename(final Resolution resolution, final String templateName, final String filename) {
        final String nameWithoutExtension = Files.getNameWithoutExtension(filename);
        final String extension = Files.getFileExtension(filename);
        return String.format(this.filenameFormat, nameWithoutExtension, resolution.getWidth(), resolution.getHeight(), templateName, extension);
    }
}
