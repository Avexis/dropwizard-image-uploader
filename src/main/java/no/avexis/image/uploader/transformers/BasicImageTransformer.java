package no.avexis.image.uploader.transformers;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BasicImageTransformer implements AbstractImageTransformer {

    public BufferedImage resizeBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageUploaderException {
        Thumbnails.Builder builder = Thumbnails.of(bufferedImage)
                .size(template.getWidth(), template.getHeight())
                .keepAspectRatio(template.isKeepAspectRatio());
        if (template.isCrop()) {
            builder = builder.crop(Positions.CENTER);
        }
        try {
            return builder.asBufferedImage();
        } catch (IOException e) {
            throw new ImageUploaderException("Could not build BufferedImage", e);
        }
    }

    public String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageUploaderException {
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
