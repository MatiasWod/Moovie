package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.User.User;


public class MoovieList {
    private final int moovieListId;
    private final int userId;
    private final String name;
    private final String description;
    private final int likes;

    public MoovieList(int moovieListId, int userId, String name, String description, int likes) {
        this.moovieListId = moovieListId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.likes = likes;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLikes() {
        return likes;
    }
}
