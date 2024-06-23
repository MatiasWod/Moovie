package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User.User;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class UserDto {

    private String username;
    private String email;
    private int role;

    private UriInfo uriInfo;

    public static UserDto fromUser(final User user, final UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.role = user.getRole();
        dto.uriInfo = uriInfo;

        return dto;
    }

    public static List<UserDto> fromUserList(final List<User> userList, final UriInfo uriInfo) {
        return userList.stream().map(u -> fromUser(u, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
