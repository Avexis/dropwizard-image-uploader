package no.avexis.image.uploader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResolutionTemplate {
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

    public String getName() {
        return name;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public boolean isCrop() {
        return crop;
    }

    public boolean isBase64() {
        return base64;
    }
}
