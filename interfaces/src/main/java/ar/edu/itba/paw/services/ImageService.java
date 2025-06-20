package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Image;

import java.util.Optional;

public interface ImageService {
    Optional<Image> getImageById(int imageId);
    void setUserImage(int userId, byte[] imageBytes, String extension);
    Optional<Image> getImageByUserId(int userId);
}
