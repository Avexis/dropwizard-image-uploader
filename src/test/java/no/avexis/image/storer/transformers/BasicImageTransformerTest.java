package no.avexis.image.storer.transformers;

import no.avexis.image.storer.exceptions.ImageStorerException;
import no.avexis.image.storer.models.ResolutionTemplate;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

import static org.junit.Assert.assertEquals;


public class BasicImageTransformerTest {

    private static final String IMAGE_1 = "image_1_1920_1200.jpg";
    private BasicImageTransformer basicImageTransformer;
    private BufferedImage bufferedImage;

    @Before
    public void buildBufferedImage() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL fileUrl = classLoader.getResource(IMAGE_1);
        bufferedImage = ImageIO.read(fileUrl);
        basicImageTransformer = new BasicImageTransformer();
    }

    @Test
    public void toBufferedImage_TransformByWidth() throws ImageStorerException {
        final int expectedWidth = 192;
        final int expectedHeight = 120;
        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 192, 192, false, false);
        final BufferedImage result = basicImageTransformer.resizeBufferedImage(bufferedImage, template);

        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }


    @Test
    public void toBufferedImage_TransformByHeight() throws ImageStorerException {
        final int expectedWidth = 64;
        final int expectedHeight = 40;
        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 100, 40, false, false);
        final BufferedImage result = basicImageTransformer.resizeBufferedImage(bufferedImage, template);

        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }

    @Test
    public void toBufferedImage_transformCrop() throws ImageStorerException {
        final int expectedWidth = 50;
        final int expectedHeight = 50;
        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 50, 50, true, false);
        final BufferedImage result = basicImageTransformer.resizeBufferedImage(bufferedImage, template);

        assertEquals(expectedWidth, result.getWidth());
        assertEquals(expectedHeight, result.getHeight());
    }

    @Test
    public void toBase64() throws ImageStorerException {
        final int expectedWidth = 5;
        final int expectedHeight = 5;
        final String expectedString = "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAFAAUDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwC34lvjNcW8rR4Zo+drlRxjsOKKKKpIg//Z";
        final ResolutionTemplate template = new ResolutionTemplate(IMAGE_1, 5, 5, true, true);
        final BufferedImage bufferedImage = basicImageTransformer.resizeBufferedImage(this.bufferedImage, template);
        assertEquals(expectedWidth, bufferedImage.getWidth());
        assertEquals(expectedHeight, bufferedImage.getHeight());

        final String base64String = basicImageTransformer.toBase64(bufferedImage, "jpg");
        assertEquals(expectedString, base64String);
    }
}