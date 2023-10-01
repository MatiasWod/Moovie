package ar.edu.itba.paw.models.Review;

public class showcaseReview extends Review{

    private final String mediaPosterPath;

    private final String mediaTitle;

    public showcaseReview(int reviewId, int userId, int mediaId, int rating, int reviewLikes, String reviewContent, String mediaPosterPath, String mediaTitle) {
        super(reviewId, userId, mediaId, rating, reviewLikes, reviewContent);
        this.mediaPosterPath = mediaPosterPath;
        this.mediaTitle = mediaTitle;
    }


    public String getMediaPosterPath() {
        return mediaPosterPath;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }
}
