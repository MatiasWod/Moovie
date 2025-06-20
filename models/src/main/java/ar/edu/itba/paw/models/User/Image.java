package ar.edu.itba.paw.models.User;

import javax.persistence.*;

@Entity
@Table(name = "userimages", uniqueConstraints = @UniqueConstraint(columnNames = {"userId"}))
public class Image {
    private int userId;
    @Id
    @Column(name = "imageId")
    private int imageId;
    @Column(name = "image")
    private byte[] image;

    public Image(){

    }

    public Image(int userId, int imageId, byte[] image) {
        this.userId = userId;
        this.imageId = imageId;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public int getImageId() {
        return imageId;
    }

    public byte[] getImage() {
        return image;
    }
}