package no.avexis.image.storer.transformers;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import no.avexis.image.storer.exceptions.ImageStorerException;
import no.avexis.image.storer.models.ResolutionTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BasicImageTransformer extends AbstractImageTransformer {

    public BufferedImage resizeBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageStorerException {
        Thumbnails.Builder builder = Thumbnails.of(bufferedImage).
                size(template.getWidth(), template.getHeight())
                .keepAspectRatio(true);
        if (template.isCrop()) {
            builder.crop(Positions.CENTER);
        }
        try {
            return builder.asBufferedImage();
        } catch (IOException e) {
            throw new ImageStorerException("Could not create BufferedImage", e);
        }
    }

    public String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageStorerException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, extension, bos);
        } catch (IOException e) {
            throw new ImageStorerException("Could not convert BufferedImage to Base64 String");
        }
        String base64bytes = Base64.getEncoder().encodeToString(bos.toByteArray());
        return String.format("data:image/%s;base64,%s", extension, base64bytes);
    }
}
