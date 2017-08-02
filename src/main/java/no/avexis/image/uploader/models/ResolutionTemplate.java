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

    public ResolutionTemplate() {
        this.name = "";
        this.width = -1;
        this.height = -1;
        this.crop = false;
        this.base64 = false;
    }

    public ResolutionTemplate(String name, Integer width, Integer height, boolean crop, boolean base64) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.crop = crop;
        this.base64 = base64;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }
}
