package no.avexis.image.uploader.models;

import no.avexis.image.uploader.exceptions.ImageUploaderException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Image {

    private UUID id;
    private String name;
    private String extension;
    private Map<String, Resolution> resolutions;

    public Image() {
        this.id = UUID.randomUUID();
        this.name = null;
        this.extension = null;
        this.resolutions = new HashMap<>();
    }

    public Image(UUID id, String name, String extension, Map<String, Resolution> resolutions) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.resolutions = resolutions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getFile(final String name) throws ImageUploaderException {
        final Resolution resolution = resolutions.get(name);
        if (resolution == null) {
            throw new ImageUploaderException(String.format("Resolution %1$s does not exist", name));
        }
        if (resolution.isBase64()) {
            return resolution.getFile();
        }
        return String.format("%1$s/%2$s", getId(), resolution.getFile());
    }
}
