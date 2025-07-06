package ar.edu.itba.paw.webapp.dto.in;

import ar.edu.itba.paw.models.Comments.CommentFeedbackType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CommentFeedbackDto {

    @Pattern(regexp ="^(LIKE|DISLIKE)$")
    @NotNull
    private String feedback;

    private String username;

    private int commentId;

    public CommentFeedbackDto() {}

    public CommentFeedbackDto(String feedback, String username, int commentId) {
        this.feedback = feedback;
        this.username = username;
        this.commentId = commentId;
    }

    public CommentFeedbackType transformToEnum() {
        return CommentFeedbackType.valueOf(feedback);
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
