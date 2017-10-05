package no.avexis.image.uploader;

import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.models.ResolutionTemplate;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;
import no.avexis.image.uploader.transformers.BasicImageTransformer;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

public class ResolutionCreatorTest {
    private BufferedImage bufferedImage;

    @Before
    public void buildBufferedImage() throws Exception {
        bufferedImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("image_1_1920_1200.jpg"));
    }


    @Test
    public void createResolutionAndBufferedImage_base64() throws Exception {
        final Method method = ResolutionCreator.class.getDeclaredMethod("createResolutionAndBufferedImage",
                BufferedImage.class, String.class, AbstractImageTransformer.class, ResolutionTemplate.class);
        method.setAccessible(true);
        final int expectedWidth = 5;
        final int expectedHeight = 5;
        final String expectedString = "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAFAAUDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwC34lvjNcW8rR4Zo+drlRxjsOKKKKpIg//Z";
        final String filename = "test.jpg";
        final AbstractImageTransformer transformer = new BasicImageTransformer();
        final ResolutionTemplate template = new ResolutionTemplate("x-small", expectedWidth, expectedHeight, true, true);

        @SuppressWarnings("unchecked") final Map.Entry<Resolution, BufferedImage> actual = (Map.Entry<Resolution, BufferedImage>) method.invoke(new ResolutionCreator(null, null),
                bufferedImage, filename, transformer, template);

        final Resolution actualResolution = actual.getKey();
        final BufferedImage actualBufferedImage = actual.getValue();

        assertEquals(expectedWidth, actualResolution.getWidth());
        assertEquals(expectedHeight, actualResolution.getHeight());
        assertTrue(actualResolution.isBase64());
        assertEquals(expectedString, actualResolution.getFile());
        assertNull(actualBufferedImage);
    }

    @Test
    public void createResolutionAndBufferedImage_file() throws Exception {
        final Method method = ResolutionCreator.class.getDeclaredMethod("createResolutionAndBufferedImage",
                BufferedImage.class, String.class, AbstractImageTransformer.class, ResolutionTemplate.class);
        method.setAccessible(true);

        final String expectedName = "small";
        final int expectedWidth = 120;
        final int expectedHeight = 75;
        final String expectedFilename = "test_" + expectedName + "_" + expectedWidth + "_" + expectedHeight + ".jpg";

        final int templateWidth = 120;
        final int templateHeight = 100;

        final String filename = "test.jpg";
        final AbstractImageTransformer transformer = new BasicImageTransformer();
        final ResolutionTemplate template = new ResolutionTemplate(expectedName, templateWidth, templateHeight, false, false);

        @SuppressWarnings("unchecked") final Map.Entry<Resolution, BufferedImage> actual = (Map.Entry<Resolution, BufferedImage>) method.invoke(new ResolutionCreator("%1$s_%4$s_%2$s_%3$s.%5$s", null),
                bufferedImage, filename, transformer, template);

        final Resolution actualResolution = actual.getKey();
        final BufferedImage actualBufferedImage = actual.getValue();

        assertEquals(expectedWidth, actualResolution.getWidth());
        assertEquals(expectedHeight, actualResolution.getHeight());
        assertFalse(actualResolution.isBase64());
        assertEquals(expectedFilename, actualResolution.getFile());

        assertNotNull(actualBufferedImage);
        assertEquals(expectedWidth, actualBufferedImage.getWidth());
        assertEquals(expectedHeight, actualBufferedImage.getHeight());
    }

    @Test
    public void createResolution() throws Exception {
        final Method method = ResolutionCreator.class.getDeclaredMethod("createResolution", ResolutionTemplate.class, BufferedImage.class);
        method.setAccessible(true);

        final ResolutionTemplate template = new ResolutionTemplate(null, null, null, false, false);
        final Resolution actual = (Resolution) method.invoke(new ResolutionCreator(null, null), template, bufferedImage);

        assertEquals(bufferedImage.getWidth(), actual.getWidth());
        assertEquals(bufferedImage.getHeight(), actual.getHeight());
        assertEquals(template.isBase64(), actual.isBase64());
    }


    @Test
    public void createFilename_Case1() throws Exception {
        final String filenameFormat = "%1$s_%4$s_%2$s_%3$s.%5$s";
        final String filename = "somefile.jpg";
        final Resolution resolution = new Resolution(60, 30, false);
        final String templateName = "large";

        final String expected = "somefile_large_60_30.jpg";

        final ResolutionCreator is = new ResolutionCreator(filenameFormat, null);
        Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
        method.setAccessible(true);
        final String actual = (String) method.invoke(is, resolution, templateName, filename);

        assertEquals(expected, actual);
    }

    @Test
    public void createFilename_Case2() throws Exception {
        final String filenameFormat = "w%2$s_h%3$s_%4$s_%1$s.%5$s";
        final String filename = "someotherfile.png";
        final Resolution resolution = new Resolution(100, 200, false);
        final String templateName = "med";

        final String expected = "w100_h200_med_someotherfile.png";

        final ResolutionCreator is = new ResolutionCreator(filenameFormat, null);
        Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
        method.setAccessible(true);
        final String actual = (String) method.invoke(is, resolution, templateName, filename);

        assertEquals(expected, actual);
    }
}