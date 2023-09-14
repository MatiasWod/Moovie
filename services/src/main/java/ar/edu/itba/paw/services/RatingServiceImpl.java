package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Rating.Rating;
import ar.edu.itba.paw.persistence.RatingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{
    @Autowired
    private RatingDao ratingDao;

    @Override
    public Optional<Rating> getRatingById(int ratingId) {
        return ratingDao.getRatingById(ratingId);
    }
}
