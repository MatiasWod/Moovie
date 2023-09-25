package ar.edu.itba.paw.models.User;

public class User {
    private final int userId;
    private final String username;
    private final String email;
    private final String password;

    private final String profilePhoto;

    public User(int userId, String username, String email, String password, String profilePhoto) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePhoto = profilePhoto;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == User.class){
            return this.userId == ((User) obj).getUserId();
        }

        return super.equals(obj);
    }
}
