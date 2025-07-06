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
    private String imageUrl;

    // URLs
    private String url;

    // Moovie Lists URLs
    private String moovieListsUrl;
    private String defaultPrivateMoovieListsUrl;
    private String publicMoovieListsUrl;
    private String privateMoovieListsUrl;

    private String likedMoovieListsUrl;
    private String followedMoovieListsUrl;

    private String likedMoovieListsReviewsUrl;
    private String likedReviewsUrl;

    private String moovieListReviewsUrl;
    private String reviewsUrl;

    private String banMessageUrl;

    public static UserDto fromUser(final User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.role = user.getRole();
        dto.moovieListCount = user.getMoovieListCount();
        dto.reviewsCount = user.getReviewsCount();
        dto.milkyPoints = user.getMilkyPoints();
        dto.hasBadge = user.isHasBadge();

        if (user.isHasPfp()) {
            dto.imageUrl = uriInfo.getBaseUriBuilder()
                    .path("images")
                    .path("{id}")
                    .build(user.getImageId())
                    .toString();
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

        dto.likedMoovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("likedByUser", user.getUsername())
                .build()
                .toString();
        dto.followedMoovieListsUrl = uriInfo.getBaseUriBuilder().path("lists")
                .queryParam("followedByUser", user.getUsername())
                .build()
                .toString();

        dto.moovieListReviewsUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews")
                .queryParam("username", user.getUsername())
                .build()
                .toString();
        dto.reviewsUrl = uriInfo.getBaseUriBuilder().path("reviews")
                .queryParam("username", user.getUsername())
                .build()
                .toString();

        dto.likedMoovieListsReviewsUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews")
                .queryParam("likedByUser", user.getUsername())
                .build()
                .toString();
        dto.likedReviewsUrl = uriInfo.getBaseUriBuilder().path("reviews")
                .queryParam("likedByUser", user.getUsername())
                .build()
                .toString();

        dto.banMessageUrl = uriInfo.getBaseUriBuilder().path("users/{username}/banMessage")
                .build(user.getUsername())
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

    public String getLikedMoovieListsReviewsUrl() {
        return likedMoovieListsReviewsUrl;
    }

    public void setLikedMoovieListsReviewsUrl(String likedMoovieListsReviewsUrl) {
        this.likedMoovieListsReviewsUrl = likedMoovieListsReviewsUrl;
    }

    public String getLikedReviewsUrl() {
        return likedReviewsUrl;
    }

    public void setLikedReviewsUrl(String likedReviewsUrl) {
        this.likedReviewsUrl = likedReviewsUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDefaultPrivateMoovieListsUrl() {
        return defaultPrivateMoovieListsUrl;
    }

    public void setDefaultPrivateMoovieListsUrl(String defaultPrivateMoovieListsUrl) {
        this.defaultPrivateMoovieListsUrl = defaultPrivateMoovieListsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }

    public void setMoovieListReviewsUrl(String moovieListReviewsUrl) {
        this.moovieListReviewsUrl = moovieListReviewsUrl;
    }

    public void setFollowedMoovieListsUrl(String followedMoovieListsUrl) {
        this.followedMoovieListsUrl = followedMoovieListsUrl;
    }

    public void setLikedMoovieListsUrl(String likedMoovieListsUrl) {
        this.likedMoovieListsUrl = likedMoovieListsUrl;
    }

    public void setMoovieListsUrl(String moovieListsUrl) {
        this.moovieListsUrl = moovieListsUrl;
    }

    public String getMoovieListsUrl() {
        return moovieListsUrl;
    }

    public String getLikedMoovieListsUrl() {
        return likedMoovieListsUrl;
    }

    public String getFollowedMoovieListsUrl() {
        return followedMoovieListsUrl;
    }

    public String getMoovieListReviewsUrl() {
        return moovieListReviewsUrl;
    }

    public String getReviewsUrl() {
        return reviewsUrl;
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
