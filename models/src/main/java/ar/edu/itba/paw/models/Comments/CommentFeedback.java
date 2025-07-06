package ar.edu.itba.paw.models.Comments;

public class CommentFeedback {
    private CommentFeedbackType commentFeedbackType;
    private String username;
    private int commentId;


    public CommentFeedback(CommentFeedbackType commentFeedbackType, String username, int commentId) {
        this.commentFeedbackType = commentFeedbackType;
        this.username = username;
        this.commentId = commentId;
    }

    public CommentFeedbackType getCommentFeedbackType() {
        return commentFeedbackType;
    }

    public void setCommentFeedbackType(CommentFeedbackType commentFeedbackType) {
        this.commentFeedbackType = commentFeedbackType;
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
