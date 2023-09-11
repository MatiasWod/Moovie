package ar.edu.itba.paw.models.Genre;

public class TVGenre extends Genre{
    private final int tvId;

    public TVGenre(int tvId, String genre) {
        super(genre);
        this.tvId = tvId;
    }

    public int getTvId() {
        return tvId;
    }
}
