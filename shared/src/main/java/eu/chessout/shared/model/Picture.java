package eu.chessout.shared.model;

public class Picture {
    private String pictureId;
    private String stringUri;

    public Picture() {
        // default public constructor
    }

    public Picture(String pictureId, String stringUri) {
        this.pictureId = pictureId;
        this.stringUri = stringUri;
    }

    // <getters and setters>

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getStringUri() {
        return stringUri;
    }

    public void setStringUri(String stringUri) {
        this.stringUri = stringUri;
    }


    // </getters and setters>
}
