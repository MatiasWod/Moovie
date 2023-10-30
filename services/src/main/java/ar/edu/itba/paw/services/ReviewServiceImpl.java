package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Transactional(readOnly = true)
    @Override
    public Review getReviewById(int reviewId) {
        return reviewDao.getReviewById( userService.tryToGetCurrentUserId(), reviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + reviewId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getReviewsByMediaId(int mediaId, int size, int pageNumber)  {
        return reviewDao.getReviewsByMediaId(userService.tryToGetCurrentUserId(), mediaId, size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getReviewsByMediaIdCount(int mediaId) {
        return reviewDao.getReviewsByMediaIdCount(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getMovieReviewsFromUser(int userId, int size, int pageNumber) {
        return reviewDao.getMovieReviewsFromUser(userService.tryToGetCurrentUserId(), userId, size, pageNumber);
    }

    @Transactional
    @Override
    public void likeReview(int reviewId) {
        getReviewById(reviewId);
        reviewDao.likeReview(userService.getInfoOfMyUser().getUserId(),reviewId);
        LOGGER.info("Succesfully liked review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void removeLikeReview(int reviewId) {
        reviewDao.removeLikeReview(userService.getInfoOfMyUser().getUserId(),reviewId);
        LOGGER.info("Succesfully removed like in review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void createReview(int mediaId, int rating, String reviewContent) {
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.createReview(userId, mediaId, rating, reviewContent);
        LOGGER.info("Succesfully created review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());

    }

    @Transactional
    @Override
    public void editReview( int mediaId, int rating, String reviewContent){
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.editReview(userId, mediaId, rating, reviewContent);
        LOGGER.info("Succesfully edited review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());
    }

}
