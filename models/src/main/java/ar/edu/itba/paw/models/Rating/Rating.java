package ar.edu.itba.paw.models.Rating;

public class Rating {
    private final int ratingId;
    private final int userId;
    private final int mediaId;
    private final int score;

    public Rating(int ratingId, int userId, int mediaId, int score) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.mediaId = mediaId;
        this.score = score;
    }

    public int getRatingId() {
        return ratingId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getScore() {
        return score;
    }
}
