package ar.edu.itba.paw.models.Cast;

public class MovieActor extends Actor{
    private final int movieId;

    public MovieActor( int actorId, String actorName, String characterName, String profilePath,int movieId) {
        super(actorId,actorName,characterName,profilePath);
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

}
