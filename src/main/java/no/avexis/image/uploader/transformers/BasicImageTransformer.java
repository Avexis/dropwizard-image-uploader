package no.avexis.image.uploader.transformers;

import com.google.common.io.Files;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BasicImageTransformer extends AbstractImageTransformer {


    public BasicImageTransformer(final BufferedImage bufferedImage, final FormDataContentDisposition formDataContentDisposition) throws ImageUploaderException {
        super(bufferedImage, formDataContentDisposition);
    }

    public BufferedImage toBufferedImage(final ResolutionTemplate template) throws ImageUploaderException {
        Thumbnails.Builder builder = Thumbnails.of(getBufferedImage()).
                size(template.getWidth(), template.getHeight())
                .keepAspectRatio(true);
        if (template.isCrop()) {
            builder.crop(Positions.CENTER);
        }
        try {
            return builder.asBufferedImage();
        } catch (IOException e) {
            throw new ImageUploaderException("Could not create BufferedImage", e);
        }
    }

    public String toBase64(final BufferedImage bufferedImage) throws ImageUploaderException {
        final String filename = getFormDataContentDisposition().getFileName();
        final String extension = Files.getFileExtension(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, extension, bos);
        } catch (IOException e) {
            throw new ImageUploaderException("Could not convert BufferedImage to Base64 String");
        }
        String base64bytes = Base64.getEncoder().encodeToString(bos.toByteArray());
        return String.format("data:image/%s;base64,%s", extension, base64bytes);
    }
}
