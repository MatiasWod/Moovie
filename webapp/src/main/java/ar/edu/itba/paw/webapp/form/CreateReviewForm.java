package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
public class CreateReviewForm {
    @Pattern(regexp="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}")
    private String userEmail;
    private int mediaId;
    private int rating;
    private String reviewContent;

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getRating() {
        return rating;
    }
}