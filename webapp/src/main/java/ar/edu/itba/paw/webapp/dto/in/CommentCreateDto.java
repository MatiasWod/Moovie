package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.QueryParam;

public class CommentCreateDto {

    @NotNull
    @Size(min = 1, max = 255)
    private String commentContent;

    @NotNull
    private int reviewId;

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
}
