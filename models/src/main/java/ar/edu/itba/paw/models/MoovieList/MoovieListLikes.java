package ar.edu.itba.paw.models.MoovieList;


import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "moovieListsLikes")
public class MoovieListLikes implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "moovieListId", referencedColumnName = "moovieListId")
    private MoovieList moovieList;

    @Id
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    public MoovieListLikes() {
    }

    public MoovieListLikes(MoovieList moovieList, User user) {
        this.moovieList = moovieList;
        this.user = user;
    }

    // Getters and setters
    public MoovieList getMoovieList() {
        return moovieList;
    }

    public void setMoovieList(MoovieList moovieList) {
        this.moovieList = moovieList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

