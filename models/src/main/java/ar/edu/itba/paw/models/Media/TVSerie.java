package ar.edu.itba.paw.models.Media;

import java.util.Date;

public class TVSerie extends Media{
    private final Date lastAirDate;
    private final Date nextEpisodeToAir;
    private final int numberOfEpisodes;
    private final int numberOfSeasons;

    public TVSerie(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview,
                   String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount,
                   String status, Date lastAirDate, Date nextEpisodeToAir, int numberOfEpisodes, int numberOfSeasons) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status);
        this.lastAirDate = lastAirDate;
        this.nextEpisodeToAir = nextEpisodeToAir;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
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
