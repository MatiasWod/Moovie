package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review getReviewById(int reviewId);
    List<Review> getReviewsByMediaId(int mediaId);


    List<Review> getMovieReviewsFromUser(int userId);
    void createReview( int mediaId, int rating, String reviewContent);
}
