package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Review.Review;

import java.util.List;

public interface ReviewService {
    Review getReviewById(int reviewId);
    //Get all the reviews for a media, contains all nescesary  data to display
    List<Review> getReviewsByMediaId(int mediaId, int size, int pageNumber) ;
    int getReviewsByMediaIdCount(int mediaId);
    //Returns all the reviews a user has made, contains all nescesary data to display
    List<Review> getMovieReviewsFromUser(int userId, int size, int pageNumber);

    //Likes a review
    void likeReview(int reviewId);
    //Removes like of a review
    void removeLikeReview(int reviewId);

    //Creates review
    void createReview( int mediaId, int rating, String reviewContent);

    void editReview( int mediaId, int rating, String reviewContent);

    void deleteReview(int reviewId);
}
