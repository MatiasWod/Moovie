package ar.edu.itba.paw.models.User;

public class User {
    private final int userId;
    private final String email;

    public User(int userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
