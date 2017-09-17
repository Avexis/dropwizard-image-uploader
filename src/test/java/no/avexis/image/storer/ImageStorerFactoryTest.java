package no.avexis.image.storer;

import no.avexis.image.storer.exceptions.ImageStorerDirectoryMissingException;
import no.avexis.image.storer.models.ResolutionTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageStorerFactory.class, File.class})
public class ImageStorerFactoryTest {

    private File mockFile;

    @Before
    public void init() throws Exception {
        mockFile = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mockFile);
    }

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


    @Test
    public void initDirectory_DirectoryExistsAndHasPermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        final ImageStorerFactory isf = new ImageStorerFactory(directory, null);
        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(true);
        when(mockFile, "canWrite").thenReturn(true);

        isf.create();
    }

    @Test
    public void initDirectory_PathExistsButIsNotDirectory() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("'%1$s' is not a directory", directory);

        final ImageStorerFactory isf = new ImageStorerFactory(directory, null);
        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(false);

        try {
            isf.create();
            fail();
        } catch (final ImageStorerDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }


    @Test
    public void initDirectory_DirectoryExistsButNoReadPermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Can not read from directory '%1$s', insufficient permissions?", directory);

        final ImageStorerFactory isf = new ImageStorerFactory(directory, null);
        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(false);

        try {
            isf.create();
            fail();
        } catch (final ImageStorerDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryExistsButNoWritePermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Can not write to directory '%1$s', insufficient permissions?", directory);

        final ImageStorerFactory isf = new ImageStorerFactory(directory, null);
        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(true);
        when(mockFile, "canWrite").thenReturn(false);

        try {
            isf.create();
            fail();
        } catch (final ImageStorerDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryDoesNotExistAndCanNotBeCreated() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Directory '%1$s' does not exist", directory);

        final ImageStorerFactory isf = new ImageStorerFactory(directory, null);
        when(mockFile, "exists").thenReturn(false);

        try {
            isf.create();
            fail();
        } catch (final ImageStorerDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryDoesNotExistAndFailedToBeCreated() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Could not create directory '%1$s'", directory);

        final ImageStorerFactory isf = new ImageStorerFactory(directory, true, null);
        when(mockFile, "exists").thenReturn(false);
        when(mockFile, "mkdirs").thenReturn(false);

        try {
            isf.create();
            fail();
        } catch (final ImageStorerDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
