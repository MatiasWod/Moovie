package ar.edu.itba.paw.models.Media;

import java.util.Date;

public class Movie extends Media{
    private final int movieId;
    private final String movieName;
    private final int runtime;
    private final int budget;
    private final int revenue;

    public Movie(int movieId,String movieName,Date releaseDate,int runtime, boolean adult, String overview, String backdropPath, String posterPath, String trailerLink,
                 int tmdbRating, int totalRating, int voteCount, String status, String originalLanguage, int budget, int revenue) {
        super(releaseDate, adult, overview, originalLanguage, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status);
        this.movieId = movieId;
        this.movieName = movieName;
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }


    public int getRuntime() {
        return runtime;
    }


    public int getBudget() {
        return budget;
    }

    public int getRevenue() {
        return revenue;
    }

}
