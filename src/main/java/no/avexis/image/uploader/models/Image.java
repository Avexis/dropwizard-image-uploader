package no.avexis.image.uploader.models;

import no.avexis.image.uploader.exceptions.ResolutionNotFoundException;

import java.util.Map;
import java.util.UUID;

public abstract class Image {

    private String name;
    private String extension;
    private Map<String, Resolution> resolutions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Map<String, Resolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(Map<String, Resolution> resolutions) {
        this.resolutions = resolutions;
    }

    public abstract UUID getUUID();

    public String getFile(final String name) throws ResolutionNotFoundException {
        final Resolution resolution = resolutions.get(name);
        if (resolution == null) {
            throw new ResolutionNotFoundException();
        }
        if (resolution.isBase64()) {
            return resolution.getFile();
        }
        return String.format("%1$s/%2$s", getUUID(), resolution.getFile());
    }
}
