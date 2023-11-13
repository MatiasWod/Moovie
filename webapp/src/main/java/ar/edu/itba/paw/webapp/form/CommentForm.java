package ar.edu.itba.paw.webapp.form;

public class CommentForm {
    private int reviewId;
    private int listMediaId;
    private String content;


    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setListMediaId(int listMediaId) {
        this.listMediaId = listMediaId;
    }

    public String getContent() {
        return content;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getListMediaId() {
        return listMediaId;
    }
}
