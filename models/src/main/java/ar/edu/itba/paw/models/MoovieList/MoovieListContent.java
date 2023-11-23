package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "moovielistscontent")
public class MoovieListContent implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "moovieListId", referencedColumnName = "moovieListId")
    private MoovieList moovieList;

    @Column
    private int mediaId;

    @Column
    private int customOrder;

    @Transient
    private boolean watched;

    MoovieListContent(){}

    public MoovieListContent(MoovieList moovieList, Media media, int mediaId, int customOrder, boolean watched) {
        this.moovieList = moovieList;
        this.mediaId = mediaId;
        this.customOrder = customOrder;
        this.watched = watched;
    }

    public void setMoovieList(MoovieList moovieList) {
        this.moovieList = moovieList;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public MoovieList getMoovieList() {
        return moovieList;
    }


    public int getCustomOrder() {
        return customOrder;
    }

    public boolean isWatched() {
        return watched;
    }
}