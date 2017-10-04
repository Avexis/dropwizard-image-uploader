package no.avexis.image.uploader;

import no.avexis.image.uploader.models.Resolution;
import no.avexis.image.uploader.transformers.AbstractImageTransformer;
import no.avexis.image.uploader.transformers.BasicImageTransformer;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


public class ImageUploaderTest {

    @Test
    public void getTransformer_TransformerExists_OK() throws Exception {
        final String key = "PNG";

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        transformers.put(key, new BasicImageTransformer());
        final ImageUploader is = new ImageUploader(null, "", transformers, null);

        Method method = is.getClass().getDeclaredMethod("getTransformer", String.class);
        method.setAccessible(true);
        assertNotNull(method.invoke(is, key));
    }

    @Test
    public void getTransformer_TransformerDoesNotExist_Nullpointer() throws Exception {
        final String key = "JPG";
        final String exceptedMessage = "No ImageTransformer for fileType " + key;

        final Map<String, AbstractImageTransformer> transformers = new HashMap<>();
        final ImageUploader is = new ImageUploader(null, "", transformers, null);

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


}
