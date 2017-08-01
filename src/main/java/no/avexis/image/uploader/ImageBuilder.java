package no.avexis.image.uploader;

import com.google.common.io.Files;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.Image;
import no.avexis.image.uploader.models.ImageFileFormat;
import no.avexis.image.uploader.models.ImageSize;
import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;
import no.avexis.image.uploader.transformers.BasicImageTransformer;
import no.avexis.image.uploader.utils.ImageSaver;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageBuilder {

    private String directory;
    private String filenameFormat;
    private List<ImageFileFormat> allowedFileTypes;
    private List<ImageSize> imageSizes;

    public ImageBuilder(String directory, String filenameFormat, List<ImageFileFormat> allowedFileTypes, List<ImageSize> imageSizes) {
        this.directory = directory;
        this.filenameFormat = filenameFormat;
        this.allowedFileTypes = allowedFileTypes;
        this.imageSizes = imageSizes;
    }

    public Image save(final InputStream inputStream, final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        validateFileFormat(formDataContentDisposition);
        Image image = new Image();
        final BufferedImage bufferedImage = readInputStream(inputStream);
        final AbstractImageTransformer transformer = new BasicImageTransformer(bufferedImage, formDataContentDisposition);
        for (ImageSize imageSize : imageSizes) {
            final BufferedImage transformedImage = transformer.toBufferedImage(imageSize);
            final Resolution resolution = createResolution(imageSize, transformedImage);
            if (imageSize.isBase64()) {
                resolution.setFile(transformer.toBase64(transformedImage));
            } else {
                final String filename = createFilename(resolution, imageSize, formDataContentDisposition);
                ImageSaver.save(directory + "/" + image.getId(), transformedImage, filename);
                resolution.setFile(filename);
            }
            image.getResolutions().put(imageSize.getName(), resolution);
        }
        return image;
    }

    private Resolution createResolution(ImageSize imageSize, BufferedImage bufferedImage) {
        Resolution resolution = new Resolution();
        resolution.setWidth(bufferedImage.getWidth());
        resolution.setHeight(bufferedImage.getHeight());
        resolution.setBase64(imageSize.isBase64());
        return resolution;
    }

    private static BufferedImage readInputStream(final InputStream inputStream) throws ImageUploaderException {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new ImageUploaderException("Could not read image InputStream", e);
        }
    }

    private void validateFileFormat(final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        final String filename = formDataContentDisposition.getFileName();
        final String extension = Files.getFileExtension(filename);
        boolean invalidFileFormat = allowedFileTypes.stream().noneMatch(fileFormat -> fileFormat.name().equalsIgnoreCase(extension));
        if (invalidFileFormat) {
            throw new ImageUploaderException(String.format("File format: %1$s is not supported", extension));
        }
    }

    private String createFilename(final Resolution resolution, final ImageSize imageSize, final FormDataContentDisposition formDataContentDisposition) {
        final String originalFilename = formDataContentDisposition.getFileName();
        return createFilename(resolution, imageSize, Files.getNameWithoutExtension(originalFilename), Files.getFileExtension(originalFilename));
    }

    private String createFilename(final Resolution resolution, final ImageSize imageSize, final String filename, final String extension) {
        return String.format(this.filenameFormat, filename, resolution.getWidth(), resolution.getHeight(), imageSize.getName(), imageSize.isCrop() ? "crop" : "", extension);
    }
}
