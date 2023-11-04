package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


public class MoovieListContent extends MoovieListContentEntity {

    private boolean watched;

    public MoovieListContent(MoovieListContentEntity mlc,
                             boolean watched) {
        super(mlc.getMediaId(), mlc.isType(), mlc.getName(), mlc.getOriginalLanguage(),
                mlc.isAdult(), mlc.getReleaseDate(), mlc.getOverview(), mlc.getBackdropPath(),
                mlc.getPosterPath(), mlc.getTrailerLink(), mlc.getTmdbRating(), mlc.getTotalRating(),
                mlc.getVoteCount(), mlc.getStatus(), mlc.getGenresModels(), mlc.getProviders(),
                mlc.getMoovieListId(), mlc.getCustomOrder());
        this.watched = watched;
    }

    public MoovieListContent(Media media, int moovieListId, int customOrder, boolean watched){
        super(media.getMediaId(), media.isType(), media.getName(), media.getOriginalLanguage(),
                media.isAdult(), media.getReleaseDate(), media.getOverview(), media.getBackdropPath(),
                media.getPosterPath(), media.getTrailerLink(), media.getTmdbRating(), media.getTotalRating(),
                media.getVoteCount(), media.getStatus(), media.getGenresModels(), media.getProviders(),
                moovieListId, customOrder);
        this.watched = watched;
    }

   MoovieListContent(){}


    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
