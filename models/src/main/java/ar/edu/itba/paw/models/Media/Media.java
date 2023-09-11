package ar.edu.itba.paw.models.Media;

import java.util.Date;

public abstract class Media {
    private final Date releaseDate;
    private final boolean adult;
    private final String overview;
    private final String originalLanguage;
    private final String backdropPath;
    private final String posterPath;
    private final String trailerLink;
    private final int tmdbRating;
    private final int totalRating;//total de todas las ratings
    private final int voteCount;//cantidad de gente que votó
    private final String status;

    public Media(Date releaseDate, boolean adult, String overview, String originalLanguage, String backdropPath, String posterPath, String trailerLink,
                 int tmdbRating, int totalRating, int voteCount, String status) {
        this.releaseDate = releaseDate;
        this.adult = adult;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public int getTmdbRating() {
        return tmdbRating;
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
