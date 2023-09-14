package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Rating.Rating;

import java.util.Optional;

public interface RatingDao {
    Optional<Rating> getRatingById(int ratingId);
}
