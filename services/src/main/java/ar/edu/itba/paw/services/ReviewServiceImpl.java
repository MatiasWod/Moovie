package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;

    @Override
    public Review getReviewById(int reviewId) {
        return reviewDao.getReviewById( userService.tryToGetCurrentUserId(), reviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + reviewId));
    }

    @Override
    public List<Review> getReviewsByMediaId(int mediaId, int size, int pageNumber)  {
        return reviewDao.getReviewsByMediaId(userService.tryToGetCurrentUserId(), mediaId, size, pageNumber);
    }

    @Override
    public int getReviewsByMediaIdCount(int mediaId) {
        return reviewDao.getReviewsByMediaIdCount(mediaId);
    }

    @Override
    public List<Review> getMovieReviewsFromUser(int userId, int size, int pageNumber) {
        return reviewDao.getMovieReviewsFromUser(userService.tryToGetCurrentUserId(), userId, size, pageNumber);
    }

    @Override
    public void likeReview(int reviewId) {
        getReviewById(reviewId);
        reviewDao.likeReview(userService.getInfoOfMyUser().getUserId(),reviewId);
    }

    @Override
    public void removeLikeReview(int reviewId) {
        reviewDao.removeLikeReview(userService.getInfoOfMyUser().getUserId(),reviewId);
    }

    @Override
    public void createReview(int mediaId, int rating, String reviewContent) {
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.createReview(userId, mediaId, rating, reviewContent);
    }
}
