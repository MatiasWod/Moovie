package ar.edu.itba.paw.models.Review;

public class Review {
    private final int reviewId;
    private final int userId;
    private final int mediaId;
    private final int ratingId;
    private final int reviewLikes;
    private final String reviewContent;

    public Review(int reviewId, int userId, int mediaId, int ratingId, int reviewLikes, String reviewContent) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.mediaId = mediaId;
        this.ratingId = ratingId;
        this.reviewLikes = reviewLikes;
        this.reviewContent = reviewContent;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getRatingId() {
        return ratingId;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
