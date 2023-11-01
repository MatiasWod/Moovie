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

@Entity
@Table(name = "moovielistscontent")
@PrimaryKeyJoinColumn(name = "mediaId")
public class MoovieListContentEntity extends Media {

    @Column
    private int moovieListId;

    @Column
    private int customOrder;

    MoovieListContentEntity(){}

    public MoovieListContentEntity(int mediaId, boolean type, String name, String originalLanguage,
                                   boolean adult, Date releaseDate, String overview, String backdropPath,
                                   String posterPath, String trailerLink, float tmdbRating, float totalRating,
                                   int voteCount, String status, List<Genre> genres, List<Provider> providers,
                                   int moovieListId, int customOrder) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
        this.moovieListId = moovieListId;
        this.customOrder = customOrder;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public int getMoovieListId() {
        return moovieListId;
    }
}
