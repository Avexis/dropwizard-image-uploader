package no.avexis.image.uploader.transformers;

import net.coobird.thumbnailator.Thumbnails;
import no.avexis.image.uploader.exceptions.ImageUploaderException;
import no.avexis.image.uploader.models.ResolutionTemplate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Thumbnails.class, Thumbnails.Builder.class, ImageIO.class})
@Ignore
/*
   PowerMockito mocking static Thumbnails and ImageIO is causing the tests to fail when run together with the rest.
   This is due to it trying to class load jpg.dll multiple times
   Not sure how to resolve this, and therefore disabling these tests until further
 */
public class BasicImageTransformerMockedTest {

    private static final String IMAGE_1 = "image_1_1920_1200.jpg";
    private BasicImageTransformer basicImageTransformer;
    private BufferedImage bufferedImage;

    @Before
    public void buildBufferedImage() throws Exception {
        bufferedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("image_1_1920_1200.jpg"));
        basicImageTransformer = new BasicImageTransformer();
    }

    @Test(expected = ImageUploaderException.class)
    public void resizeBufferedImage_builderThrowsIoException_ImageUploaderException() throws Exception {
        PowerMockito.mockStatic(Thumbnails.class);
        @SuppressWarnings("unchecked")
        Thumbnails.Builder<BufferedImage> mockBuilder = PowerMockito.mock(Thumbnails.Builder.class);
        when(mockBuilder, "size", anyInt(), anyInt()).thenReturn(mockBuilder);
        when(mockBuilder, "keepAspectRatio", anyBoolean()).thenReturn(mockBuilder);
        when(mockBuilder, "crop", any()).thenReturn(mockBuilder);
        when(mockBuilder, "asBufferedImage").thenThrow(IOException.class);
        when(Thumbnails.of(any(BufferedImage.class))).thenReturn(mockBuilder);

        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 1, 1, true, false);

        final String expectedMessage = "Could not build BufferedImage";
        try {
            basicImageTransformer.resizeBufferedImage(bufferedImage, template);
            fail();
        } catch (final ImageUploaderException e) {
            assertEquals(expectedMessage, e.getMessage());
            throw e;
        }
    }

    @Test(expected = ImageUploaderException.class)
    public void toBase64_ImageIoThrowsIoException_ImageUploaderException() throws Exception {
        PowerMockito.mockStatic(ImageIO.class);
        when(ImageIO.write(any(BufferedImage.class), anyString(), any(ByteArrayOutputStream.class))).thenThrow(IOException.class);
        final String expectedMessage = "Could not convert BufferedImage to Base64 String";
        try {
            basicImageTransformer.toBase64(bufferedImage, "jpg");
            fail();
        } catch (final ImageUploaderException e) {
            assertEquals(expectedMessage, e.getMessage());
            throw e;
        }
    }
}