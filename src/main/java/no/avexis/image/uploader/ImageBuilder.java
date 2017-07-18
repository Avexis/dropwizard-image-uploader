package no.avexis.image.uploader;

import com.google.common.io.Files;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.Image;
import no.avexis.image.uploader.models.ImageFileFormat;
import no.avexis.image.uploader.models.ImageSize;
import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.utils.ImageSaver;
import no.avexis.image.uploader.utils.ImageTransformer;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class ImageBuilder {

    private String directory;
    private String filenameFormat;
    private ImageFileFormat forcedFileFormat;
    private List<ImageFileFormat> allowedFileTypes;
    private List<ImageSize> imageSizes;

    public ImageBuilder(String directory, String filenameFormat, ImageFileFormat forcedFileFormat, List<ImageFileFormat> allowedFileTypes, List<ImageSize> imageSizes) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.forcedFileFormat = forcedFileFormat;
        this.allowedFileTypes = allowedFileTypes;
        this.imageSizes = imageSizes;
    }

    public Image save(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        validateFileFormat(formDataContentDisposition);
        final ImageTransformer transformer = new ImageTransformer(inputStream, formDataContentDisposition);
        if (forcedFileFormat != null) {
            transformer.forceFileFormat(forcedFileFormat);
        }
        Image image = new Image();
        image.setId(UUID.randomUUID());
        for (ImageSize imageSize : imageSizes) {
            final Resolution resolution = createAndSaveResolution(transformer, image.getId(), imageSize, formDataContentDisposition);
            image.getResolutions().put(imageSize.getName(), resolution);
        }
        return image;
    }

    private Resolution createAndSaveResolution(final ImageTransformer transformer, final UUID uuid, final ImageSize imageSize, final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        final Resolution resolution = transformer.createResolution(imageSize);
        String file;
        if (resolution.isBase64()) {
            file = transformer.toBase64(resolution);
        } else {
            file = createFilename(imageSize, formDataContentDisposition);
            final BufferedImage transformedImage = transformer.toBufferedImage(resolution);
            ImageSaver.save(directory + "/" + uuid, transformedImage, file);
        }
        resolution.setFile(file);
        return resolution;
    }

    private void validateFileFormat(final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        final String filename = formDataContentDisposition.getFileName();
        final String extension = Files.getFileExtension(filename);
        boolean invalidFileFormat = allowedFileTypes.stream().noneMatch(fileFormat -> fileFormat.name().equalsIgnoreCase(extension));
        if (invalidFileFormat) {
            throw new ImageUploaderException(String.format("File format: %1$s is not supported", extension));
        }
    }

    private String createFilename(final ImageSize imageSize, final FormDataContentDisposition formDataContentDisposition) {
        final String originalFilename = formDataContentDisposition.getFileName();
        return createFilename(imageSize, Files.getNameWithoutExtension(originalFilename), Files.getFileExtension(originalFilename));
    }

    private String createFilename(final ImageSize size, final String filename, final String extension) {
        return String.format(this.filenameFormat, filename, size.getWidth(), size.getHeight(), size.getName(), size.isCrop() ? "crop" : "", extension);
    }
}
