package ar.edu.itba.paw.models.Media;

import java.util.Date;

public class Media {
    private final int mediaId;
    private final boolean type;
    private final String name;
    private final String originalLanguage;
    private final boolean adult;
    private final Date releaseDate;
    private final String overview;
    private final String backdropPath;
    private final String posterPath;
    private final String trailerLink;
    private final float tmdbRating;
    private final int totalRating;//total de todas las ratings
    private final int voteCount;//cantidad de gente que votó
    private final String status;

    public Media(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview,
                 String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount, String status) {
        this.mediaId = mediaId;
        this.type = type;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
    }

    public int getMediaId() {
        return mediaId;
    }

    public boolean isType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public boolean isAdult() {
        return adult;
    }

    public Date getReleaseDate() {
        return releaseDate;
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

    public String getTrailerLink() {
        return trailerLink;
    }

    public float getTmdbRating() {
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
