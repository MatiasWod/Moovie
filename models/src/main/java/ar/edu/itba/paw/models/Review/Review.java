package ar.edu.itba.paw.models.Review;

public class Review {
    private final int reviewId;
    private final int userId;
    private final String username;
    private final int mediaId;
    private final int rating;
    private final int reviewLikes;
    private final String mediaPosterPath;
    private final String mediaTitle;
    private final String reviewContent;

    public Review(int reviewId, int userId, String username, int mediaId, int rating, int reviewLikes, String mediaTitle, String mediaPosterPath, String reviewContent) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.username = username;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewLikes = reviewLikes;
        this.mediaPosterPath = mediaPosterPath;
        this.mediaTitle = mediaTitle;
        this.reviewContent = reviewContent;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
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

    public String getMediaPosterPath() {
        return mediaPosterPath;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
