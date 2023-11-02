package ar.edu.itba.paw.models.MoovieList;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

public class MoovieListCard extends MoovieListCardEntity{

    MoovieListCardUserStatus mlcUS;
    public MoovieListCard(MoovieListCardEntity mlcE , MoovieListCardUserStatus mlcUS ){
        super(mlcE.getMoovieListId(), mlcE.getName(), mlcE.getUsername(), mlcE.getDescription(), mlcE.getLikeCount(), mlcE.getFollowerCount(), mlcE.getType(), mlcE.getSize(), mlcE.getMoviesAmount(), mlcE.getImages());
        this.mlcUS = mlcUS;
    }

    public int getCurrentUserWatchAmount() {
        return mlcUS.getCurrentUserWatchAmount();
    }

    public boolean isCurrentUserHasLiked() {
        return mlcUS.isCurrentUserHasLiked();
    }

    public boolean isCurrentUserHasFollowed() {
        return mlcUS.isCurrentUserHasFollowed();
    }

}
