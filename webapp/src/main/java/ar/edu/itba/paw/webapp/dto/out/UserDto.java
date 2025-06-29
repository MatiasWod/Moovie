package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.User.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserDto {

    private int id;
    private String username;
    private int role;

    // URLs
    private String url;
    private String profileUrl;
    private String reviewsUrl;
    private String moovieListsUrl;
    private String moovieListReviewsUrl;

    public static UserDto fromUser(final User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id = user.getUserId();
        dto.username = user.getUsername();
        dto.role = user.getRole();

        // URLs
        dto.url = uriInfo.getBaseUriBuilder().path("/users/{username}").build(user.getUsername()).toString();
        dto.profileUrl = uriInfo.getBaseUriBuilder().path("/profiles/{username}").build(user.getUsername()).toString();
        dto.reviewsUrl = uriInfo.getBaseUriBuilder().path("/reviews")
                .queryParam("userId", user.getUserId())
                .build()
                .toString();
        dto.moovieListsUrl = uriInfo.getBaseUriBuilder().path("/lists")
                .queryParam("ownerUsername", user.getUsername())
                .build()
                .toString();
        dto.moovieListReviewsUrl = uriInfo.getBaseUriBuilder().path("/moovieListReviews")
                .queryParam("userId", user.getUserId())
                .build()
                .toString();
        return dto;
    }

    public static List<UserDto> fromUserList(final List<User> userList, final UriInfo uriInfo) {
        return userList.stream().map(u -> fromUser(u, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
