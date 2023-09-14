package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.Review;

import java.util.Optional;

public interface ReviewDao {
    Optional<Review> getReviewById(int reviewId);
}
