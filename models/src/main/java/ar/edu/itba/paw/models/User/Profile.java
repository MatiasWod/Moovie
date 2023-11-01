package ar.edu.itba.paw.models.User;

import javax.persistence.*;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
    private int userId;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private int role;
    @Transient
    private int moovieListCount;
    @Transient
    private int likedMoovieListCount;
    @Transient
    private int reviewsCount;

    public Profile(){

    }

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

    public int getMoovieListCount() {
        return moovieListCount;
    }

    public int getLikedMoovieListCount() {
        return likedMoovieListCount;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }
}
