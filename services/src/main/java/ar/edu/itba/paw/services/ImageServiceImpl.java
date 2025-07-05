package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidTypeException;
import ar.edu.itba.paw.exceptions.NoFileException;
import ar.edu.itba.paw.models.User.Image;
import ar.edu.itba.paw.persistence.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private ImageDao imageDao;

    final static int MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    @Override
    public Image getImageById(int imageId, int size) {
        Image image = imageDao.getImageById(imageId).orElseThrow(NoFileException::new);

        if (size > 0) {
            try {
                byte[] resized = resizeImage(image.getImage(), size);
                return new Image(image.getUserId(), image.getImageId(), resized);
            } catch (IOException e) {
                throw new RuntimeException("Failed to resize image", e);
            }
        }

        return image;
    }

    private byte[] resizeImage(byte[] originalImageBytes, int targetSize) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(originalImageBytes)) {
            BufferedImage originalImage = ImageIO.read(bais);

            if (originalImage == null)
                throw new IOException("Invalid image format");

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            double aspectRatio = (double) originalWidth / originalHeight;

            int newWidth, newHeight;
            if (originalWidth > originalHeight) {
                newWidth = targetSize;
                newHeight = (int) (targetSize / aspectRatio);
            } else {
                newHeight = targetSize;
                newWidth = (int) (targetSize * aspectRatio);
            }

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(resizedImage, "png", baos);
                return baos.toByteArray();
            }
        }
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
}
