package ar.edu.itba.paw.models.MoovieList;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoovieListCard {
    private final int moovieListId;
    private final String name;
    private final String username;
    private final String description;
    private final int likeCount;
    private final boolean currentUserHasLiked;
    private final int followerCount;
    private final boolean currentUserHasFollowed;
    private final int type;
    private final int size;
    private final int moviesAmount;
    private final int currentUserWatchAmount;
    private List<String> images;

    public MoovieListCard(int moovieListId, String name,  String username, String description, int likeCount,  boolean currentUserHasLiked, int followerCount, boolean currentUserHasFollowed, int type, int size, int moviesAmount, int currentUserWatchAmount, String images) {
        this.moovieListId = moovieListId;
        this.name = name;
        this.username = username;
        this.description = description;
        this.likeCount = likeCount;
        this.currentUserHasLiked = currentUserHasLiked;
        this.followerCount = followerCount;
        this.currentUserHasFollowed = currentUserHasFollowed;
        this.type = type;
        this.size = size;
        this.moviesAmount = moviesAmount;
        this.currentUserWatchAmount = currentUserWatchAmount;
        if(images!=null){
            String[] aux = images.replaceAll("[{}]","").split(",");
            this.images = new ArrayList<>(Arrays.asList(aux));
        }else{
            this.images = new ArrayList<>();
        }
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public String getName(){
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public boolean isCurrentUserHasFollowed() {
        return currentUserHasFollowed;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public int getMoviesAmount() {
        return moviesAmount;
    }

    public int getCurrentUserWatchAmount() {
        return currentUserWatchAmount;
    }

    public List<String> getImages() {
        return images;
    }
}
