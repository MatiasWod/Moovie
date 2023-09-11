package ar.edu.itba.paw.models.Genre;

public class MovieGenre extends Genre{
    private final int movieId;

    public MovieGenre(int movieId, String genre) {
        super(genre);
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

}
