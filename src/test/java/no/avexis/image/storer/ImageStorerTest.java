package no.avexis.image.storer;

import no.avexis.image.storer.models.Resolution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageStorerTest {

    @Test
    @DisplayName("CreateFilename_FormatIsCorrectlyInserted")
    void createFilename_FormatIsCorrectlyInserted() throws Exception {
        final String filenameFormat = "%1$s_%4$s_%2$s_%3$s.%5$s";
        final String filename = "somefile.jpg";
        final Resolution resolution = new Resolution();
        resolution.setWidth(60);
        resolution.setHeight(30);
        final String templateName = "large";

        final String expected = "somefile_large_60_30.jpg";

        final ImageStorer is = new ImageStorer("", filenameFormat, null, null, false);
        Method method = is.getClass().getDeclaredMethod("createFilename", Resolution.class, String.class, String.class);
        method.setAccessible(true);
        final String actual = (String) method.invoke(is, resolution, templateName, filename);

        assertEquals(expected, actual);
    }

}
