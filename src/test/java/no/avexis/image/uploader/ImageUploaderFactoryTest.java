package no.avexis.image.uploader;

import no.avexis.image.uploader.models.ResolutionTemplate;
import no.avexis.image.uploader.storers.AbstractImageStorer;
import no.avexis.image.uploader.storers.LocalImageStorer;
import no.avexis.image.uploader.transformers.BasicImageTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocalImageStorer.class, File.class})
public class ImageUploaderFactoryTest {

    @Test
    public void build() throws Exception {
        File mockFile = PowerMockito.mock(File.class);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isDirectory()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
        when(mockFile.canWrite()).thenReturn(true);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mockFile);

        final AbstractImageStorer ais = new LocalImageStorer("", false);

        final List<ResolutionTemplate> templates = new ArrayList<>();
        final ImagUploaderFactory isf = new ImagUploaderFactory("", ais, templates);

        isf.build();
    }


    @Test
    public void addImageTransformer_AddedTransformerIsUppercaseAndIsFound() throws Exception {
        final ImagUploaderFactory isf = new ImagUploaderFactory(null, null);
        final String key = "waff";
        isf.addImageTransformer(key, new BasicImageTransformer());

        assertFalse(isf.imageTransformerExists(key));
        assertTrue(isf.imageTransformerExists(key.toUpperCase()));
    }

    @Test
    public void addImageTransformer_CanRemoveExistingTransformer() throws Exception {
        final ImagUploaderFactory isf = new ImagUploaderFactory(null, null);
        final String key = "JPG";
        assertTrue(isf.imageTransformerExists(key));
        assertTrue(isf.removeImageTransformer(key));
    }


    @Test
    public void getFilenameFormat() throws Exception {

        final String defaultFormat = "%1$s_%4$s_%2$s_%3$s.%5$s";
        final String customFormat = "%1$s.%5$s";
        final ImagUploaderFactory isfDefault = new ImagUploaderFactory(null, null);
        final ImagUploaderFactory isfCustom = new ImagUploaderFactory(customFormat, null, null);

        assertEquals(defaultFormat, isfDefault.getFilenameFormat());
        assertEquals(customFormat, isfCustom.getFilenameFormat());
    }

    @Test
    public void getTemplates() throws Exception {
        final ResolutionTemplate rt = new ResolutionTemplate();
        final ImagUploaderFactory isf = new ImagUploaderFactory(null, Collections.singletonList(rt));

        final List<ResolutionTemplate> templates = isf.getTemplates();
        assertEquals(1, templates.size());
        assertEquals(rt, templates.get(0));
    }
}
