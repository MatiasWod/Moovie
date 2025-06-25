package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.User.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

//TODO: Faltan relaciones (comments, reviews, moovieLists, moovieListReviews, ...)
public class UserDto {

    private int id;
    private String username;
    private int role;

    private String url;
    private String profileUrl;

    public static UserDto fromUser(final User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id=user.getUserId();
        dto.username = user.getUsername();
        dto.role = user.getRole();
        dto.url = uriInfo.getBaseUriBuilder().path("/users/{username}").build(user.getUsername()).toString();
        dto.profileUrl = uriInfo.getBaseUriBuilder().path("/profiles/{username}").build(user.getUsername()).toString();
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
