package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Optional<Review> getReviewById(int reviewId);
    List<Review> getReviewForMoovieListFromUser(int moovieListId, int userId);
}
