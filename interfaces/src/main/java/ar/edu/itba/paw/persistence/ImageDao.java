package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Image;

import java.util.Optional;

public interface ImageDao {
    Optional<Image> getImageById(int imageId);
    int setUserImage(int userId, byte[] imageBytes);
    Optional<Image> getImageByUserId(int userId);
}
