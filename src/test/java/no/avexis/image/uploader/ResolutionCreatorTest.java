package no.avexis.image.uploader;

import no.avexis.image.uploader.models.Resolution;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ResolutionCreatorTest {

    @Test
    public void CreateFilename_Case1() throws Exception {
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
    public void CreateFilename_Case2() throws Exception {
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