package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.Image;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Primary
@Repository
public class ImageHibernateDaoImpl implements ImageDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Image> getImageById(int imageId) {
        final TypedQuery<Image> query = entityManager.createQuery("from Image where imageId = :imageId", Image.class);
        query.setParameter("imageId", imageId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void setUserImage(int userId, byte[] imageBytes) {
        Optional<Image> existingImage = getImageByUserId(userId);
        existingImage.ifPresent(image -> entityManager.remove(image));
        int imageId = Objects.hash(userId, LocalDateTime.now());
        final Image toInsertImage = new Image(userId, imageId, imageBytes);
        entityManager.persist(toInsertImage);
    }

    @Override
    public Optional<Image> getImageByUserId(int userId) {
        final TypedQuery<Image> query = entityManager.createQuery("from Image where userId = :userId", Image.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst();
    }

}
