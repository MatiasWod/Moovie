package ar.edu.itba.paw.webapp.form;

public class CommentForm {
    private int reviewId;
    private int mediaId;
    private String content;


    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public int getReviewId() {
        return reviewId;
    }
}
