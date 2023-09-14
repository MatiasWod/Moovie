package ar.edu.itba.paw.models.Review;

public class Review {
    private final int reviewId;
    private final int userId;
    private final int mediaId;
    private final int rating;
    private final int reviewLikes;
    private final String reviewContent;

    public Review(int reviewId, int userId, int mediaId, int rating, int reviewLikes, String reviewContent) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.mediaId = mediaId;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
