package ar.edu.itba.paw.models.Media;

import java.util.Date;

public class TVSerie extends Media{
    private final int tvId;
    private final String tvName;
    private final Date lastAirDate;
    private final Date nextEpisodeToAir;
    private final int numberOfEpisodes;
    private final int numberOfSeasons;

    public TVSerie(int tvId, String tvName, Date releaseDate, Date lastAirDate, Date nextEpisodeToAir, String originalLanguage, boolean adult, String overview, String backdropPath, String posterPath, String trailerLink,int tmdbRating,
                   int totalRating, int voteCount, String status, int numberOfEpisodes, int numberOfSeasons) {
        super(releaseDate, adult, overview, originalLanguage, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status);
        this.tvId = tvId;
        this.tvName = tvName;
        this.lastAirDate = lastAirDate;
        this.nextEpisodeToAir = nextEpisodeToAir;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getTvId() {
        return tvId;
    }
    public String getTvName() {
        return tvName;
    }

    public Date getLastAirDate() {
        return lastAirDate;
    }

    public Date getNextEpisodeToAir() {
        return nextEpisodeToAir;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
}
