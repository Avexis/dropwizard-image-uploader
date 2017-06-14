package no.avexis.image.uploader;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageWrapper {
    @JsonProperty
    private String name;
    @JsonProperty
    private Integer position;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private boolean crop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
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
}
