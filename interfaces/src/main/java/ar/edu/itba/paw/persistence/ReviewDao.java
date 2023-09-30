package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Optional<Review> getReviewById(int reviewId);
    List<Review> getReviewsByMediaId(int mediaId);
    List<Review> getReviewForMoovieListFromUser(int moovieListId, int userId);
    List<Review> getMovieReviewsFromUser(int userId);
    Review createReview(int userId, int mediaId, int rating, String reviewContent);

}
