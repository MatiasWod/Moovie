package ar.edu.itba.paw.models.Genre;

public class TVGenre extends Genre{
    private final int tvId;

    public TVGenre(int tvId, String[] genres) {
        super(genres);
        this.tvId = tvId;
    }

    public int getTvId() {
        return tvId;
    }
}
