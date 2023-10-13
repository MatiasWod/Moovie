package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Optional<Review> getReviewById(int currentUserId, int reviewId);
    List<Review> getReviewsByMediaId(int currentUserId, int mediaId, int size, int pageNumber);
    List<Review> getMovieReviewsFromUser(int currentUserId, int userId, int size, int pageNumber);
    int getReviewsByMediaIdCount(int mediaId);

    void createReview(int userId, int mediaId, int rating, String reviewContent);
    void deleteReview(int reviewId);

    void likeReview(int userId, int reviewId);
    void removeLikeReview(int userId, int reviewId);
}
