package ar.edu.itba.paw.models.User;

public class Profile {
    private final int userId;
    private final String username;
    private final String email;
    private final int role;
    private final int moovieListCount;
    private final int likedMoovieListCount;
    private final int reviewsCount;

    public Profile(int userId, String username, String email, int role, int moovieListCount, int likedMoovieListCount, int reviewsCount) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.moovieListCount = moovieListCount;
        this.likedMoovieListCount = likedMoovieListCount;
        this.reviewsCount = reviewsCount;
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

    public int getRole() {
        return role;
    }

    public int getmoovieListCount() {
        return moovieListCount;
    }

    public int getLikedMoovieListCount() {
        return likedMoovieListCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }
}
