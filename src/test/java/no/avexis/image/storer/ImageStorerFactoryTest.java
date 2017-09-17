package no.avexis.image.storer;

import no.avexis.image.storer.models.ResolutionTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.doNothing;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageStorerFactory.class})
public class ImageStorerFactoryTest {

    @Test
    public void create() throws Exception {
        final String directory = "directory";
        final String filenameFormat = "filenameFormat";
        final List<ResolutionTemplate> templates = new ArrayList<>();
        final boolean runParallel = false;
        final ImageStorerFactory isfSpy = PowerMockito.spy(new ImageStorerFactory(directory, false, filenameFormat, templates, runParallel));
        doNothing().when(isfSpy, "initDirectory");

        final ImageStorer imageStorer = isfSpy.create();

        final Field directoryField = imageStorer.getClass().getDeclaredField("directory");
        directoryField.setAccessible(true);
        assertEquals("directory is passed on", directory, directoryField.get(imageStorer));


        Field filenameFormatField = imageStorer.getClass().getDeclaredField("filenameFormat");
        filenameFormatField.setAccessible(true);
        assertEquals("filenameFormat is passed on", filenameFormat, filenameFormatField.get(imageStorer));


        Field templatesField = imageStorer.getClass().getDeclaredField("templates");
        templatesField.setAccessible(true);
        assertEquals("templates is passed on", templates, templatesField.get(imageStorer));

        Field runParallelField = imageStorer.getClass().getDeclaredField("runParallel");
        runParallelField.setAccessible(true);
        assertEquals("runParallel is passed on", runParallel, runParallelField.get(imageStorer));
    }

    public void addImageTransformer() {
    }
}
