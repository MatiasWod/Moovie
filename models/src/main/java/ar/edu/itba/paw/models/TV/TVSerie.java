package ar.edu.itba.paw.models.TV;

import java.util.Date;

public class TVSerie {
    private final int tvId;
    private final String tvName;
    private final Date releaseDate;
    private final Date lastAirDate;
    private final Date nextEpisodeToAir;
    private final String originalLanguage;
    private final boolean adult;
    private final String overview;
    private final String backdropPath;
    private final String posterPath;
    private final String trailerPath;
    private final int totalRating;//total de todas las ratings
    private final int voteCount;//cantidad de gente que votó
    private final String status;
    private final int numberOfEpisodes;
    private final int numberOfSeasons;

    public TVSerie(int tvId, String tvName, Date releaseDate, Date lastAirDate, Date nextEpisodeToAir, String originalLanguage, boolean adult, String overview, String backdropPath, String posterPath, String trailerPath, int totalRating, int voteCount, String status, int numberOfEpisodes, int numberOfSeasons) {
        this.tvId = tvId;
        this.tvName = tvName;
        this.releaseDate = releaseDate;
        this.lastAirDate = lastAirDate;
        this.nextEpisodeToAir = nextEpisodeToAir;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerPath = trailerPath;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getTvId() {
        return tvId;
    }

    public String getTvName() {
        return tvName;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Date getLastAirDate() {
        return lastAirDate;
    }

    public Date getNextEpisodeToAir() {
        return nextEpisodeToAir;
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

    public int getTotalRating() {
        return totalRating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
}
