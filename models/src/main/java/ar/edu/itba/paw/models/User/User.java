package ar.edu.itba.paw.models.User;

import ar.edu.itba.paw.models.MoovieList.MoovieListFollowers;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_userid_seq")
    @SequenceGenerator(sequenceName = "users_userid_seq", name = "users_userid_seq", allocationSize = 1)
    private Integer userId;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private int role;

    @OneToMany(mappedBy = "user")
    final private Set<MoovieListLikes> likes = new HashSet<>();

    @OneToMany(mappedBy = "user")
    final private Set<MoovieListFollowers> followers = new HashSet<>();

    //Para hibernate
    public User(){

    }

    public User(int userId, String username, String email, String password, int role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Builder builder) {
        this.userId = builder.userId;
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.role =  builder.role;
    }

    public Set<MoovieListFollowers> getFollowers() {
        return followers;
    }

    public Set<MoovieListLikes> getLikes() {
        return likes;
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

    public int getRole() {
        return role;
    }

    public static class Builder {
        private final String email;
        private final String username;
        private final String password;
        private final int role;
        private Integer userId = null;

        public Builder(String username, String email, String password, int role) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
