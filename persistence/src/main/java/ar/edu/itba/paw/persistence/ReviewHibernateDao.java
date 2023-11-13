package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import org.hibernate.SQLQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class ReviewHibernateDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Review> getReviewById(int currentUserId, int reviewId) {
        Review review = Optional.ofNullable(em.find(Review.class, reviewId)).orElseThrow(() -> new MoovieListNotFoundException("Review by id: " + reviewId + " not found"));

        String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewslikes rl WHERE rl.reviewid= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("reviewId", reviewId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object obj = query.getSingleResult();

        review.setHasLiked((boolean) obj);

        return Optional.of(review);
    }



    @Override
    public List<Review> getReviewsByMediaId(int currentUserId, int mediaId, int size, int pageNumber) {
        final Query query = em.createQuery("SELECT r FROM Review r WHERE r.mediaId = :mediaId");
        query.setParameter("mediaId", mediaId);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);

        @SuppressWarnings("unchecked")
        List<Review> reviews = query.getResultList();

        for (Review review : reviews) {
            String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewslikes rl WHERE rl.reviewid= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter("reviewId", review.getReviewId());
            query2.setParameter("userId", currentUserId);

            query2.unwrap(SQLQuery.class);

            Object obj = query2.getSingleResult();

            review.setHasLiked((boolean) obj);
        }

        return reviews;
    }

    public int getReviewsByMediaIdCount(int mediaId) {
        return ((Number) em.createQuery("SELECT COUNT(r) FROM Review r WHERE r.mediaId = :mediaId").setParameter("mediaId", mediaId).getSingleResult()).intValue();
    }


    @Override
    public List<Review> getMovieReviewsFromUser(int currentUserId, int userId, int size, int pageNumber) {
        final Query query = em.createQuery("SELECT r FROM Review r WHERE r.userId = :userId");
        query.setParameter("userId", userId);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);

        @SuppressWarnings("unchecked")
        List<Review> reviews = query.getResultList();

        for (Review review : reviews) {
            String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewslikes rl WHERE rl.reviewid= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter("reviewId", review.getReviewId());
            query2.setParameter("userId", currentUserId);

            query2.unwrap(SQLQuery.class);

            Object obj = query2.getSingleResult();

            review.setHasLiked((boolean) obj);
        }

        return reviews;
    }

    @Override
    public Optional<MoovieListReview> getMoovieListReviewById(int currentUserId, int moovieListReviewId) {
        MoovieListReview review = Optional.ofNullable(em.find(MoovieListReview.class, moovieListReviewId)).orElseThrow(() -> new MoovieListNotFoundException("Review by id: " + moovieListReviewId + " not found"));

        String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM moovielistsreviewslikes rl WHERE rl.moovieListReviewId= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("reviewId", moovieListReviewId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object obj = query.getSingleResult();

        review.setCurrentUserHasLiked((boolean) obj);

        return Optional.of(review);
    }

    @Override
    public List<MoovieListReview> getMoovieListReviewsByMoovieListId(int currentUserId, int moovieListId, int size, int pageNumber) {
        final Query query = em.createQuery("SELECT r FROM MoovieListReview r WHERE r.moovieListId = :moovieListId");
        query.setParameter("moovieListId", moovieListId);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);

        @SuppressWarnings("unchecked")
        List<MoovieListReview> reviews = query.getResultList();

        for (MoovieListReview review : reviews) {
            String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM moovieListsReviewsLikes rl WHERE rl.moovieListReviewId= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter("reviewId", review.getMoovieListReviewId());
            query2.setParameter("userId", currentUserId);

            query2.unwrap(SQLQuery.class);

            Object obj = query2.getSingleResult();

            review.setCurrentUserHasLiked((boolean) obj);
        }

        return reviews;
    }

    @Override
    public int getMoovieListReviewByMoovieListIdCount(int moovieListId) {
        return ((Number) em.createQuery("SELECT COUNT(r) FROM MoovieListReview r WHERE r.moovieListId = :moovieListId").setParameter("moovieListId", moovieListId).getSingleResult()).intValue();
    }

    @Override
    public List<MoovieListReview> getMoovieListReviewsFromUser(int currentUserId, int userId, int size, int pageNumber) {
        final Query query = em.createQuery("SELECT r FROM MoovieListReview r WHERE r.userId = :userId");
        query.setParameter("userId", userId);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);

        @SuppressWarnings("unchecked")
        List<MoovieListReview> reviews = query.getResultList();

        for (MoovieListReview review : reviews) {
            String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM moovieListsReviewsLikes rl WHERE rl.moovieListReviewId= :moovieListReviewId  AND rl.userid = :userId) THEN true ELSE false END";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter("moovieListReviewId", review.getMoovieListReviewId());
            query2.setParameter("userId", currentUserId);

            query2.unwrap(SQLQuery.class);

            Object obj = query2.getSingleResult();

            review.setCurrentUserHasLiked((boolean) obj);
        }

        return reviews;
    }



    @Override
    public void createReview(int userId, int mediaId, int rating, String reviewContent, ReviewTypes type) {
        try {
            if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
                Review review = new Review(userId, mediaId, rating, reviewContent);
                em.persist(review);
            }else{
                MoovieListReview review = new MoovieListReview(userId, mediaId,  reviewContent);
                em.persist(review);
            }
        } catch (Exception e) {
            throw new UnableToInsertIntoDatabase("An error occurred while creating the review.", e);
        }
    }

    @Override
    public void editReview(int userId, int mediaId, int rating, String reviewContent, ReviewTypes type) {
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            Review review = em.createQuery("SELECT r FROM Review r WHERE r.userId = :userId AND r.mediaId = :mediaId", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("mediaId", mediaId)
                    .getSingleResult();
            if(review != null) {
                review.setRating(rating);
                review.setReviewContent(reviewContent);
                em.merge(review);
            }
        }else{
            MoovieListReview review = em.createQuery("SELECT r FROM MoovieListReview r WHERE r.userId = :userId AND r.moovieListId = :mediaId", MoovieListReview.class)
                    .setParameter("userId", userId)
                    .setParameter("mediaId", mediaId)
                    .getSingleResult();
            if(review != null) {
                review.setReviewContent(reviewContent);
                em.merge(review);
            }
        }


    }

    @Override
    public void deleteReview(int reviewId, ReviewTypes type) {
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()){
            Review review = em.find(Review.class, reviewId);
            em.remove(review);
        }else{
            MoovieListReview review = em.find(MoovieListReview.class, reviewId);
            em.remove(review);
        }
    }

    @Override
    public void likeReview(int userId, int reviewId, ReviewTypes type) {
        Query query =null;
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            query = em.createNativeQuery("INSERT INTO reviewslikes (userid, reviewid) VALUES (:userId, :reviewId)");

        }else{
            query = em.createNativeQuery("INSERT INTO moovieListsReviewsLikes (userid, moovieListReviewId) VALUES (:userId, :reviewId)");
        }
        query.setParameter("userId", userId);
        query.setParameter("reviewId", reviewId);
        query.executeUpdate();
    }


    @Override
    public void removeLikeReview(int userId, int reviewId, ReviewTypes type) {
        Query query =null;
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            query = em.createNativeQuery("DELETE FROM reviewslikes WHERE userid = :userId AND reviewid = :reviewId");
        }else{
            query = em.createNativeQuery("DELETE FROM moovieListsReviewsLikes WHERE userid = :userId AND reviewid = :reviewId");
        }
        query.setParameter("userId", userId);
        query.setParameter("reviewId", reviewId);
        query.executeUpdate();
    }
}
