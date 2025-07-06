package ar.edu.itba.paw.webapp.dto.out;

import javax.ws.rs.core.UriInfo;

public class UserCommentFeedbackDto {
    private int commentId;
    private String username;
    private String commentUrl;
    private String userUrl;
    private String url;
    private String feedback;

    public UserCommentFeedbackDto() {}

    public UserCommentFeedbackDto(int commentId, String username, String url, String feedback, UriInfo uriInfo) {
        this.commentId = commentId;
        this.username = username;
        this.url = url;
        this.feedback = feedback;

        this.commentUrl = uriInfo.getBaseUriBuilder().path("comments/{commentId}").build(commentId).toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("users/{username}").build(username).toString();
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
