package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private UserService userService;

    @Override
    public Review getReviewById(int reviewId) {
        return reviewDao.getReviewById( userService.tryToGetCurrentUserId(), reviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + reviewId));
    }

    @Override
    public List<Review> getReviewsByMediaId(int mediaId) {
        return reviewDao.getReviewsByMediaId(userService.tryToGetCurrentUserId(), mediaId);
    }


    @Override
    public List<Review> getMovieReviewsFromUser(int userId) {
        return reviewDao.getMovieReviewsFromUser(userService.tryToGetCurrentUserId(), userId);
    }

    @Override
    public void likeReview(int reviewId) {
        return;
    }

    @Override
    public void createReview(int mediaId, int rating, String reviewContent) {
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.createReview(userId, mediaId, rating, reviewContent);
    }
}
