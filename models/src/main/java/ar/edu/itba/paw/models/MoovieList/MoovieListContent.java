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

    @Id
    @ManyToOne
    @JoinColumn(name = "mediaId", referencedColumnName = "mediaId")
    private Media media;

    @Column
    private int customOrder;

    @Transient
    private boolean watched;

    MoovieListContent(){}

    public MoovieListContent(MoovieList moovieList, Media media, int customOrder, boolean watched) {
        this.moovieList = moovieList;
        this.media = media;
        this.customOrder = customOrder;
        this.watched = watched;
    }

    public void setMoovieList(MoovieList moovieList) {
        this.moovieList = moovieList;
    }

    public void setMedia(Media media) {
        this.media = media;
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

    public Media getMedia() {
        return media;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public boolean isWatched() {
        return watched;
    }
}