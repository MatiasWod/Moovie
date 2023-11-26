package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ReviewDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional(readOnly = true)
    @Override
    public MoovieListReview getMoovieListReviewById(int moovieListReviewId) {
        return reviewDao.getMoovieListReviewById(userService.tryToGetCurrentUserId(), moovieListReviewId).orElseThrow( ()-> new ReviewNotFoundException("Review not found for id: " + moovieListReviewId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getMoovieListReviewsByMoovieListId(int moovieListId, int size, int pageNumber) {
        return reviewDao.getMoovieListReviewsByMoovieListId(userService.tryToGetCurrentUserId(), moovieListId,size,pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getMoovieListReviewByMoovieListIdCount(int moovieListId) {
        return reviewDao.getMoovieListReviewByMoovieListIdCount(moovieListId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getMoovieListReviewsFromUser(int userId, int size, int pageNumber) {
        return reviewDao.getMoovieListReviewsFromUser(userService.tryToGetCurrentUserId(), userId,size,pageNumber);
    }

    @Transactional
    @Override
    public void likeReview(int reviewId, ReviewTypes type) {
        reviewDao.likeReview(userService.getInfoOfMyUser().getUserId(),reviewId,type);
        LOGGER.info("Succesfully liked review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void removeLikeReview(int reviewId, ReviewTypes type) {
        reviewDao.removeLikeReview(userService.getInfoOfMyUser().getUserId(),reviewId,type);
        LOGGER.info("Succesfully removed like in review: {}, user: {}.", reviewId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void createReview(int mediaId, int rating, String reviewContent, ReviewTypes type) {
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.createReview(userId, mediaId, rating, reviewContent,type);
        LOGGER.info("Succesfully created review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());

    }

    @Transactional
    @Override
    public void editReview( int mediaId, int rating, String reviewContent, ReviewTypes type){
        int userId = userService.getInfoOfMyUser().getUserId();
        reviewDao.editReview(userId, mediaId, rating, reviewContent,type);
        LOGGER.info("Succesfully edited review in media: {}, user: {}.", mediaId , userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void deleteReview(int reviewId, ReviewTypes type){
        User currentUser = userService.getInfoOfMyUser();
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()){
            Review review = reviewDao.getReviewById(currentUser.getUserId(), reviewId)
                    .orElseThrow(() -> new NoSuchElementException("Review not found"));
            if (currentUser.getUserId()!=review.getUserId()) {
                throw new InvalidAccessToResourceException("User is not owner of the Review.");
            }
        } else{
            MoovieListReview review = reviewDao.getMoovieListReviewById(currentUser.getUserId(), reviewId)
                    .orElseThrow(() -> new NoSuchElementException("Review not found"));
            if (currentUser.getUserId()!=review.getUserId()) {
                throw new InvalidAccessToResourceException("User is not owner of the Review.");
            }
        }

        reviewDao.deleteReview(reviewId,type);
        LOGGER.info("Succesfully deleted review: {}, user: {}.", reviewId , userService.getInfoOfMyUser().getUserId());
    }


    @Transactional(readOnly = true)
    @Override
    public Review getReviewByMediaIdAndUsername(int mediaId, int userId, ReviewTypes type){
        return reviewDao.getReviewByMediaIdAndUsername(mediaId, userId, type);
    }
}
