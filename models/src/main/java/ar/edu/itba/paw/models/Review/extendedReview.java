package ar.edu.itba.paw.models.Review;

public class extendedReview extends Review{

    private final String userName;

    public extendedReview(int reviewId, int userId, int mediaId, int rating, int reviewLikes, String reviewContent, String userName) {
        super(reviewId, userId, mediaId, rating, reviewLikes, reviewContent);
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

}
