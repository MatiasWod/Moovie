package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "moovielistscontent")
@PrimaryKeyJoinColumn(name = "mediaId")
public class MoovieListContent extends Media {

    @Column
    private int moovieListId;

    @Column
    private int customOrder;

    @Transient
    private boolean watched;

    MoovieListContent(){}

    public MoovieListContent(int mediaId, boolean type, String name, String originalLanguage,
                                   boolean adult, Date releaseDate, String overview, String backdropPath,
                                   String posterPath, String trailerLink, float tmdbRating, float totalRating,
                                   int voteCount, String status, List<Genre> genres, List<Provider> providers,
                                   int moovieListId, int customOrder) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
        this.moovieListId = moovieListId;
        this.customOrder = customOrder;
    }

    public MoovieListContent(Media media, int moovieListId, int customOrder, boolean watched){
        super(media.getMediaId(), media.isType(), media.getName(), media.getOriginalLanguage(),
                media.isAdult(), media.getReleaseDate(), media.getOverview(), media.getBackdropPath(),
                media.getPosterPath(), media.getTrailerLink(), media.getTmdbRating(), media.getTotalRating(),
                media.getVoteCount(), media.getStatus(), media.getGenresModels(), media.getProviders());
        this.watched = watched;
    }

    public MoovieListContent(MoovieListContent mlc,
                             boolean watched) {
        super(mlc.getMediaId(), mlc.isType(), mlc.getName(), mlc.getOriginalLanguage(),
                mlc.isAdult(), mlc.getReleaseDate(), mlc.getOverview(), mlc.getBackdropPath(),
                mlc.getPosterPath(), mlc.getTrailerLink(), mlc.getTmdbRating(), mlc.getTotalRating(),
                mlc.getVoteCount(), mlc.getStatus(), mlc.getGenresModels(), mlc.getProviders());
        this.customOrder = mlc.customOrder;
        this.moovieListId = mlc.moovieListId;
        this.watched = watched;
    }


    public int getCustomOrder() {
        return customOrder;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public void setMoovieListId(int moovieListId) {
        this.moovieListId = moovieListId;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }
}