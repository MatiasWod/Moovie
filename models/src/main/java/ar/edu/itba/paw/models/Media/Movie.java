package ar.edu.itba.paw.models.Media;

import java.util.Date;

public class Movie extends Media{
    private final int runtime;
    private final long budget;
    private final long revenue;
    private int directorId;
    private final String director;

    public Movie(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview,
                 String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount,
                 String status, int runtime, long budget, long revenue, int directorId, String director, String genres, String providerNames, String providerLogos) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providerNames, providerLogos);
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
        this.directorId = directorId;
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getDirector() {
        return director;
    }
}
