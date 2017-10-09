package no.avexis.image.uploader.storers;

import no.avexis.image.uploader.exceptions.ImageUploaderDirectoryMissingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalImageStorer.class, File.class})
public class LocalImageUploaderTest {
    private File mockFile;

    @Before
    public void init() throws Exception {
        mockFile = PowerMockito.mock(File.class);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mockFile);
    }

    @Test
    public void initDirectory_DirectoryExistsAndHasPermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(true);
        when(mockFile, "canWrite").thenReturn(true);

        new LocalImageStorer(directory, null);
    }

    @Test
    public void initDirectory_PathExistsButIsNotDirectory() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("'%1$s' is not a directory", directory);

        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(false);

        try {
            new LocalImageStorer(directory, null);
            fail();
        } catch (final ImageUploaderDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }


    @Test
    public void initDirectory_DirectoryExistsButNoReadPermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Can not read from directory '%1$s', insufficient permissions?", directory);

        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(false);

        try {
            new LocalImageStorer(directory, null);
            fail();
        } catch (final ImageUploaderDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryExistsButNoWritePermissions() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Can not write to directory '%1$s', insufficient permissions?", directory);

        when(mockFile, "exists").thenReturn(true);
        when(mockFile, "isDirectory").thenReturn(true);
        when(mockFile, "canRead").thenReturn(true);
        when(mockFile, "canWrite").thenReturn(false);

        try {
            new LocalImageStorer(directory, null);
            fail();
        } catch (final ImageUploaderDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryDoesNotExistAndCanNotBeCreated() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Directory '%1$s' does not exist", directory);

        when(mockFile, "exists").thenReturn(false);

        try {
            new LocalImageStorer(directory, false);
            fail();
        } catch (final ImageUploaderDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void initDirectory_DirectoryDoesNotExistAndFailedToBeCreated() throws Exception {
        final String directory = "/mydir/myfolder";
        final String expectedMessage = String.format("Could not build directory '%1$s'", directory);

        when(mockFile, "exists").thenReturn(false);
        when(mockFile, "mkdirs").thenReturn(false);

        try {
            new LocalImageStorer(directory, true);
            fail();
        } catch (final ImageUploaderDirectoryMissingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
