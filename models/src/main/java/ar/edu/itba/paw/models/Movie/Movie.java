package ar.edu.itba.paw.models.Movie;

import java.util.Date;

public class Movie {
    private final int movieId;
    private final String movieName;
    private final Date releaseDate;
    private final int runtime;
    private final String originalLanguage;
    private final boolean adult;
    private final String overview;
    private final String backdropPath;
    private final String posterPath;
    private final String trailerPath;
    private final int budget;
    private final int revenue;
    private final int totalRating;//total de todas las ratings
    private final int voteCount;//cantidad de gente que votó
    private final String status;

    public Movie(int movieId, String movieName, Date releaseDate, int runtime, String originalLanguage, boolean adult, String overview, String backdropPath, String posterPath, String trailerPath, int budget, int revenue, int totalRating, int voteCount, String status) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerPath = trailerPath;
        this.budget = budget;
        this.revenue = revenue;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public int getBudget() {
        return budget;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }
}
