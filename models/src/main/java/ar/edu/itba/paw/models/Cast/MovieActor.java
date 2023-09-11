package ar.edu.itba.paw.models.Cast;

public class MovieActor extends Actor{
    private final int movieId;

    public MovieActor(int movieId, String actorName, String characterName, String profilePath) {
        super(actorName,characterName,profilePath);
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

}
