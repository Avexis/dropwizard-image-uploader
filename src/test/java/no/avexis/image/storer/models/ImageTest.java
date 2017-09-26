package no.avexis.image.storer.models;

import no.avexis.image.storer.exceptions.ImageStorerException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class ImageTest {

    public static class GettersTest {

        private static Image defaultImage;
        private static Image customImage;
        private static UUID id;
        private static String name;
        private static String extension;
        private static Map<String, Resolution> resolutions;

        @BeforeClass
        public static void init() {
            defaultImage = new Image();
            id = UUID.randomUUID();
            name = "test name";
            extension = ".jpg";
            resolutions = new HashMap<>();
            resolutions.put("large", new Resolution());
            customImage = new Image(id, name, extension, resolutions);
        }

        @Test
        public void getId() throws Exception {
            assertNotNull(defaultImage.getId());
            assertEquals(id, customImage.getId());
        }

        @Test
        public void getName() throws Exception {
            assertNull(defaultImage.getName());
            assertEquals(name, customImage.getName());
        }

        @Test
        public void getExtension() throws Exception {
            assertNull(defaultImage.getExtension());
            assertEquals(extension, customImage.getExtension());
        }

        @Test
        public void getResolutions() throws Exception {
            assertTrue(defaultImage.getResolutions().isEmpty());
            assertEquals(1, customImage.getResolutions().size());
        }
    }

    public static class SettersTest {

        private Image image;

        @Before
        public void init() {
            image = new Image();
        }

        @Test
        public void setId() throws Exception {
            final UUID uuid = UUID.randomUUID();
            assertNotEquals(uuid, image.getId());
            image.setId(uuid);
            assertEquals(uuid, image.getId());
        }


        @Test
        public void setName() throws Exception {
            final String name = "Some name";
            assertNull(image.getName());
            image.setName(name);
            assertEquals(name, image.getName());
        }


        @Test
        public void setExtension() throws Exception {
            final String extension = ".png";
            assertNull(image.getExtension());
            image.setExtension(extension);
            assertEquals(extension, image.getExtension());
        }

        @Test
        public void setResolutions() throws Exception {
            final Map<String, Resolution> map = new HashMap<>();
            map.put("large", new Resolution());
            assertTrue(image.getResolutions().isEmpty());
            image.setResolutions(map);
            assertEquals(map, image.getResolutions());
        }
    }

    @Test(expected = ImageStorerException.class)
    public void getFile_ResolutionDoesNotExist_ImageStorerException() throws Exception {
        final String name = "large";
        final String expectedMessage = String.format("Resolution %1$s does not exist", name);
        final Image image = new Image();
        try {
            image.getFile(name);
            fail();
        } catch (final ImageStorerException e) {
            assertEquals(expectedMessage, e.getMessage());
            throw e;
        }
    }

    @Test
    public void getFile_FileIsBase64_Base64String() throws Exception {
        final String name = "large";
        final String expectedBase64 = "base64:expectedBase64String";
        final Image image = new Image();
        final Resolution resolution = new Resolution(1, 1, true, expectedBase64);
        image.getResolutions().put(name, resolution);

        assertEquals(expectedBase64, image.getFile(name));
    }


    @Test
    public void getFile_FileIsStoredImage_PathString() throws Exception {
        final String name = "large";
        final String expectedFilename = "testFile.jpg";
        final Image image = new Image();
        final Resolution resolution = new Resolution(1, 1, false, expectedFilename);
        image.getResolutions().put(name, resolution);

        final String expected = String.format("%1$s/%2$s", image.getId(), expectedFilename);
        assertEquals(expected, image.getFile(name));
    }
}