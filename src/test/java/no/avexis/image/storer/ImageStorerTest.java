package no.avexis.image.storer;

import no.avexis.image.storer.models.Resolution;
import no.avexis.image.storer.transformers.AbstractImageTransformer;
import no.avexis.image.storer.transformers.BasicImageTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ImageStorerTest {

    @Test
    @DisplayName("Get Transformer Exists")
    void getTransformer_TransformerExists_OK() throws Exception {
        final String key = "PNG";

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        transformers.put(key, new BasicImageTransformer());
        final ImageStorer is = new ImageStorer("", "", transformers, null, false);

        Method method = is.getClass().getDeclaredMethod("getTransformer", String.class);
        method.setAccessible(true);
        assertNotNull(method.invoke(is, key));
    }

    @Test
    @DisplayName("Get Transformer Does Not Exists")
    void getTransformer_TransformerDoesNotExist_Nullpointer() throws Exception {
        final String key = "JPG";
        final String exceptedMessage = "No ImageTransformer for fileType " + key;

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        final ImageStorer is = new ImageStorer("", "", transformers, null, false);

        Method method = is.getClass().getDeclaredMethod("getTransformer", String.class);
        method.setAccessible(true);
        final Throwable exception = assertThrows(InvocationTargetException.class, () -> method.invoke(is, key));
        final Throwable innerException = exception.getCause();

        assertEquals(NullPointerException.class, innerException.getClass());
        assertEquals(exceptedMessage, innerException.getMessage());
    }

    @Nested
    @DisplayName("Create Filename")
    class CreateFilename {
        @Test
        @DisplayName("Case 1")
        void CreateFilename_Case1() throws Exception {
            final String filenameFormat = "%1$s_%4$s_%2$s_%3$s.%5$s";
            final String filename = "somefile.jpg";
            final Resolution resolution = new Resolution(60, 30, false);
            final String templateName = "large";

            final String expected = "somefile_large_60_30.jpg";

            final ImageStorer is = new ImageStorer("", filenameFormat, null, null, false);
            Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
            method.setAccessible(true);
            final String actual = (String) method.invoke(is, resolution, templateName, filename);

            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("Case 2")
        void CreateFilename_Case2() throws Exception {
            final String filenameFormat = "w%2$s_h%3$s_%4$s_%1$s.%5$s";
            final String filename = "someotherfile.png";
            final Resolution resolution = new Resolution(100, 200, false);
            final String templateName = "med";

            final String expected = "w100_h200_med_someotherfile.png";

            final ImageStorer is = new ImageStorer("", filenameFormat, null, null, false);
            Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
            method.setAccessible(true);
            final String actual = (String) method.invoke(is, resolution, templateName, filename);

            assertEquals(expected, actual);
        }

    }

}
