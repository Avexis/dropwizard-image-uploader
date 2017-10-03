package no.avexis.image.saver;

import no.avexis.image.saver.models.Resolution;
import no.avexis.image.saver.transformers.AbstractImageTransformer;
import no.avexis.image.saver.transformers.BasicImageTransformer;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class ImageSaverTest {

    @Test
    public void getTransformer_TransformerExists_OK() throws Exception {
        final String key = "PNG";

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        transformers.put(key, new BasicImageTransformer());
        final ImageSaverController is = new ImageSaverController(null, "", transformers, null);

        Method method = is.getClass().getDeclaredMethod("getTransformer", String.class);
        method.setAccessible(true);
        assertNotNull(method.invoke(is, key));
    }

    @Test
    public void getTransformer_TransformerDoesNotExist_Nullpointer() throws Exception {
        final String key = "JPG";
        final String exceptedMessage = "No ImageTransformer for fileType " + key;

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        final ImageSaverController is = new ImageSaverController(null, "", transformers, null);

        Method method = is.getClass().getDeclaredMethod("getTransformer", String.class);
        method.setAccessible(true);
        try {
            method.invoke(is, key);
            fail();
        } catch (final InvocationTargetException e) {
            final Throwable innerException = e.getCause();
            assertEquals(NullPointerException.class, innerException.getClass());
            assertEquals(exceptedMessage, innerException.getMessage());
        }
    }

    @Test
    public void CreateFilename_Case1() throws Exception {
        final String filenameFormat = "%1$s_%4$s_%2$s_%3$s.%5$s";
        final String filename = "somefile.jpg";
        final Resolution resolution = new Resolution(60, 30, false);
        final String templateName = "large";

        final String expected = "somefile_large_60_30.jpg";

        final ImageSaverController is = new ImageSaverController(null, filenameFormat, null, null);
        Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
        method.setAccessible(true);
        final String actual = (String) method.invoke(is, resolution, templateName, filename);

        assertEquals(expected, actual);
    }

    @Test
    public void CreateFilename_Case2() throws Exception {
        final String filenameFormat = "w%2$s_h%3$s_%4$s_%1$s.%5$s";
        final String filename = "someotherfile.png";
        final Resolution resolution = new Resolution(100, 200, false);
        final String templateName = "med";

        final String expected = "w100_h200_med_someotherfile.png";

        final ImageSaverController is = new ImageSaverController(null, filenameFormat, null, null);
        Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
        method.setAccessible(true);
        final String actual = (String) method.invoke(is, resolution, templateName, filename);

        assertEquals(expected, actual);
    }

}
