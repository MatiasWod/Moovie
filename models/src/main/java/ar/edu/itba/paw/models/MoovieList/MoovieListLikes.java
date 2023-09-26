package ar.edu.itba.paw.models.MoovieList;

public class MoovieListLikes {
    private final int moovieListId;
    private final int userId;

    public MoovieListLikes(int moovieListId, int userId) {
        this.moovieListId = moovieListId;
        this.userId = userId;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }
}
