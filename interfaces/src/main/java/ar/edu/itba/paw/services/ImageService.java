package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.Image;

import java.util.Optional;

public interface ImageService {
    Image getImageById(int imageId, int size);
    int setUserImage(int userId, byte[] imageBytes, String extension);
}
