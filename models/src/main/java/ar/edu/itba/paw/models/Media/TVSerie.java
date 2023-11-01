package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tv")
@PrimaryKeyJoinColumn(name = "mediaId")
public class TVSerie extends Media{

    @Column
    private Date lastAirDate;

    @Column
    private Date nextEpisodeToAir;

    @Column
    private int numberOfEpisodes;

    @Column
    private int numberOfSeasons;

    TVSerie(){

    }

    public TVSerie(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview, String backdropPath, String posterPath, String trailerLink, float tmdbRating, float totalRating, int voteCount, String status, List<Genre> genres, List<Provider> providers, Date lastAirDate, Date nextEpisodeToAir, int numberOfEpisodes, int numberOfSeasons) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
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
