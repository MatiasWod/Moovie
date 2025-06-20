package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTypeException;
import ar.edu.itba.paw.exceptions.NoFileException;
import ar.edu.itba.paw.models.User.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ImageDao imageDao;

    final static int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    @Override
    public Optional<Image> getImageById(int imageId) {
        return imageDao.getImageById(imageId);
    }

    @Transactional
    @Override
    public void setUserImage(int userId, byte[] imageBytes, String extension) {
        if (imageBytes.length > 0) {
            if (imageBytes.length > MAX_IMAGE_SIZE) {
                throw new InvalidTypeException("File is too big (Max is 5MB).");
            }

            if (extension != null || extension.equals("png") || extension.equals("jpg")
                    || extension.equals("jpeg") || extension.equals("gif")) {
                imageDao.setUserImage(userId, imageBytes);
            } else {
                throw new InvalidTypeException("File is not of type image");
            }
        } else {
            throw new NoFileException("No file was selected");
        }

    }

    @Transactional
    @Override
    public Optional<Image> getImageByUserId(int userId) {
        return imageDao.getImageByUserId(userId);
    }
}
