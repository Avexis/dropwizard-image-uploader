package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

class ImageSize {
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private boolean crop;
    @JsonProperty
    private boolean base64;

    protected String getName() {
        return name;
    }

    protected Integer getWidth() {
        return width;
    }

    protected Integer getHeight() {
        return height;
    }

    protected boolean isCrop() {
        return crop;
    }

    protected boolean isBase64() {
        return base64;
    }
}
