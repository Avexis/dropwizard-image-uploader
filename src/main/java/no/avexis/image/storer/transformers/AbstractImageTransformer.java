package no.avexis.image.storer.transformers;

import no.avexis.image.storer.exceptions.ImageStorerException;
import no.avexis.image.storer.models.ResolutionTemplate;

import java.awt.image.BufferedImage;

public abstract class AbstractImageTransformer {

    public abstract BufferedImage toBufferedImage(final BufferedImage bufferedImage, final ResolutionTemplate template) throws ImageStorerException;

    public abstract String toBase64(final BufferedImage bufferedImage, final String extension) throws ImageStorerException;
}
