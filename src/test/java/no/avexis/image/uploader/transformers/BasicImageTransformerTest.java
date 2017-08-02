package no.avexis.image.uploader.transformers;

import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicImageTransformerTest {

    private static final String IMAGE_1 = "image_1_1920_1200.jpg";
    private BasicImageTransformer basicImageTransformer;
    private BufferedImage bufferedImage;

    @Before
    void buildBufferedImage() throws Exception {
        bufferedImage = ImageIO.read(this.getClass().getResourceAsStream(IMAGE_1));
        basicImageTransformer = new BasicImageTransformer();
    }

    @Test
    @DisplayName("TransformBufferdImage")
    void toBufferedImage_Transform() throws ImageUploaderException {
        final int expectedWidth = 100;
        final int expectedHeight = 62;
        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 100, 100, false, false);
        final BufferedImage result = basicImageTransformer.toBufferedImage(bufferedImage, template);

        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }

    @Test
    @DisplayName("TransformAndCropBufferedImage")
    void toBufferedImage_transformCrop() {

    }

    @Test
    void toBase64() {
    }

}