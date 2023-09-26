package ar.edu.itba.paw.models.MoovieList;

public class MoovieListFollowers {
    private final int moovieListId;
    private final int userId;

    public MoovieListFollowers(int moovieListId, int userId) {
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
