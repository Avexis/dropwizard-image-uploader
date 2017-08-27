package no.avexis.image.storer.models;

public class Resolution {
    private int width;
    private int height;
    private boolean base64;
    private String file;

    public Resolution() {
        this.width = -1;
        this.height = -1;
        this.base64 = false;
        this.file = null;
    }

    public Resolution(int width, int height, boolean base64, String file) {
        this.width = width;
        this.height = height;
        this.base64 = base64;
        this.file = file;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBase64() {
        return base64;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }

    /**
     * If base64 is true file will be the base64 string, if not file corresponds to the filename
     *
     * @return base64 string or filename
     */
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
