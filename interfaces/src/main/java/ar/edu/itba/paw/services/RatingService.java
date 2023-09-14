package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Rating.Rating;

import java.util.Optional;

public interface RatingService {
    Optional<Rating> getRatingById(int ratingId);
}
