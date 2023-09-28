package ar.edu.itba.paw.services;

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
    public Optional<Review> getReviewById(int reviewId) {
        return reviewDao.getReviewById(reviewId);
    }

    @Override
    public List<Review> getReviewsByMediaId(int mediaId) {
        return reviewDao.getReviewsByMediaId(mediaId);
    }

    @Override
    public List<Review> getReviewForMoovieListFromUser(int moovieListId, int userId) {
        return reviewDao.getReviewForMoovieListFromUser(moovieListId,userId);
    }

    @Override
    public Review createReview(int mediaId, int rating, String reviewContent) {
        int userId = userService.getInfoOfMyUser().getUserId();
        return reviewDao.createReview(userId, mediaId, rating, reviewContent);
    }
}
