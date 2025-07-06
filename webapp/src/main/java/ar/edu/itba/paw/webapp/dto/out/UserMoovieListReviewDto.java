package ar.edu.itba.paw.webapp.dto.out;

import javax.ws.rs.core.UriInfo;

public class UserMoovieListReviewDto {
    private int moovieListReviewId;
    private String username;
    private String moovieListReviewUrl;
    private String userUrl;
    private String url;

    public UserMoovieListReviewDto() {}

    public UserMoovieListReviewDto(int moovieListReviewId, String username, String url, UriInfo uriInfo) {
        this.moovieListReviewId = moovieListReviewId;
        this.username = username;
        this.url = url;

        this.moovieListReviewUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews/{moovieListReviewId}").build(moovieListReviewId).toString();
        this.userUrl = uriInfo.getBaseUriBuilder().path("users/{username}").build(username).toString();
    }

    public int getReviewId() {
        return moovieListReviewId;
    }

    public void setReviewId(int moovieListReviewId) {
        this.moovieListReviewId = moovieListReviewId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewUrl() {
        return moovieListReviewUrl;
    }

    public void setReviewUrl(String moovieListReviewUrl) {
        this.moovieListReviewUrl = moovieListReviewUrl;
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
