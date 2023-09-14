package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewDao reviewDao;

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        return reviewDao.getReviewById(reviewId);
    }
}
