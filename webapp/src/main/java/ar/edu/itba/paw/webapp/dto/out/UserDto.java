package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserDto {
    private String username;
    private int role;
    private int moovieListCount;
    private int reviewsCount;
    private int milkyPoints;
    private boolean hasBadge;
    private String profilePictureUrl;

    // URLs
    private String url;

    // Moovie Lists URLs
    private String moovieListsUrl;
    private String defaultPrivateMoovieListsUrl;
    private String publicMoovieListsUrl;
    private String privateMoovieListsUrl;

    private String moovieListReviewsUrl;
    private String reviewsUrl;

    public static UserDto fromUser(final User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.role = user.getRole();
        dto.moovieListCount = user.getMoovieListCount();
        dto.reviewsCount = user.getReviewsCount();
        dto.milkyPoints = user.getMilkyPoints();
        dto.hasBadge = user.isHasBadge();

        if (user.isHasPfp()) {
            dto.profilePictureUrl = uriInfo.getBaseUriBuilder().path("users/{username}/image")
                    .build(user.getUsername()).toString();
        }

        // URLs
        dto.url = uriInfo.getBaseUriBuilder().path("users/{username}").build(user.getUsername()).toString();

        // Moovie Lists URLs
        dto.moovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("ownerUsername", user.getUsername())
                .build()
                .toString();
        dto.defaultPrivateMoovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("ownerUsername", user.getUsername())
                .queryParam("type", MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType())
                .build()
                .toString();
        dto.publicMoovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("ownerUsername", user.getUsername())
                .queryParam("type", MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType())
                .build()
                .toString();
        dto.privateMoovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("ownerUsername", user.getUsername())
                .queryParam("type", MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType())
                .build()
                .toString();

        dto.moovieListReviewsUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews")
                .queryParam("userId", user.getUserId())
                .build()
                .toString();
        dto.reviewsUrl = uriInfo.getBaseUriBuilder().path("reviews")
                .queryParam("userId", user.getUserId())
                .build()
                .toString();
        return dto;
    }

    public static List<UserDto> fromUserList(List<User> userList, UriInfo uriInfo) {
        return userList.stream().map(m -> fromUser(m, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getMoovieListCount() {
        return moovieListCount;
    }

    public void setMoovieListCount(int moovieListCount) {
        this.moovieListCount = moovieListCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public int getMilkyPoints() {
        return milkyPoints;
    }

    public void setMilkyPoints(int milkyPoints) {
        this.milkyPoints = milkyPoints;
    }

    public boolean isHasBadge() {
        return hasBadge;
    }

    public void setHasBadge(boolean hasBadge) {
        this.hasBadge = hasBadge;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPictureUrl() {
        return profilePictureUrl;
    }

    public void setPictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getDefaultPrivateMoovieListsUrl() {
        return defaultPrivateMoovieListsUrl;
    }

    public void setDefaultPrivateMoovieListsUrl(String defaultPrivateMoovieListsUrl) {
        this.defaultPrivateMoovieListsUrl = defaultPrivateMoovieListsUrl;
    }

    public String getPublicMoovieListsUrl() {
        return publicMoovieListsUrl;
    }

    public void setPublicMoovieListsUrl(String publicMoovieListsUrl) {
        this.publicMoovieListsUrl = publicMoovieListsUrl;
    }

    public String getPrivateMoovieListsUrl() {
        return privateMoovieListsUrl;
    }

    public void setPrivateMoovieListsUrl(String privateMoovieListsUrl) {
        this.privateMoovieListsUrl = privateMoovieListsUrl;
    }
}
