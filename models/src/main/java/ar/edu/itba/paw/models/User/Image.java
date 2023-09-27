package ar.edu.itba.paw.models.User;

public class Image {
    private final int userId;
    private final byte[] image;


    public Image(int userId, byte[] image) {
        this.userId = userId;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public byte[] getImage() {
        return image;
    }
}