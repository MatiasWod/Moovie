package ar.edu.itba.paw.webapp.dto.out;

import javax.ws.rs.core.UriInfo;

public class UserReviewDto { // Renamed from UserReviewIdDto for consistency
    private int reviewId;
    private String username;
    private String reviewUrl; // New field for the review's URL
    private String userUrl;   // New field for the user's URL
    private String url;       // Existing field, kept for consistency if needed for self-URL

    public UserReviewDto() {}

    public UserReviewDto(int reviewId, String username, String url, UriInfo uriInfo) {
        this.reviewId = reviewId;
        this.username = username;
        this.url = url;

        this.reviewUrl = uriInfo.getBaseUriBuilder().path("reviews/{reviewId}").build(reviewId).toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("users/{username}").build(username).toString();
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
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
}
