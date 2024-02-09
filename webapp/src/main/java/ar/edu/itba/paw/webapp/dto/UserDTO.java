package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User.User;

public class UserDTO {
    private String username;
    private String email;

    private int role;

    public static UserDTO fromUser(final User user){
        final UserDTO dto = new UserDTO();

        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.role = user.getRole();

        return dto;
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
