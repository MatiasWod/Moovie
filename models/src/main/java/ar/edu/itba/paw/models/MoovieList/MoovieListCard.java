package ar.edu.itba.paw.models.MoovieList;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name="moovielists")
public class MoovieListCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielists_moovielistid_seq")
    @SequenceGenerator(sequenceName = "moovielists_moovielistid_seq", name = "moovielists_moovielistid_seq", allocationSize = 1)
    @Column(name = "moovielistId")
    private int moovieListId;

    @Column
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int type;

    @Formula("(SELECT users.username FROM users WHERE users.userid = userid)")
    private String username;

    @Formula("(SELECT COUNT(*) FROM moovielistslikes mll WHERE mll.moovielistid = moovielistid)")
    private int likeCount;

    @Formula("(SELECT COUNT(*) FROM moovielistsfollows mll WHERE mll.moovielistid = moovielistid)")
    private int followerCount;

    @Formula("(SELECT COUNT(*) FROM moovieListsContent mlc WHERE mlc.moovieListId = moovieListId)")
    private int size;

    @Formula("(SELECT COUNT(*) FROM moovielistsContent mlc INNER JOIN media m ON mlc.mediaid = m.mediaid WHERE m.type = false AND mlc.moovieListid = moovieListId)")
    private int moviesAmount;

    @Formula("( SELECT ARRAY_AGG(m.posterPath) FROM moovielistscontent mlc INNER JOIN media m ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = moovieListId LIMIT 4 )")
    private String images;

    @Transient
    private int currentUserWatchAmount;

    @Transient
    private boolean currentUserHasLiked;

    @Transient
    private boolean currentUserHasFollowed;

    public MoovieListCard(){}

    public MoovieListCard(int moovieListId, int userId, String name, String username, String description, int likeCount, int followerCount, int type, int size, int moviesAmount, String images) {
        this.moovieListId = moovieListId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.username = username;
        this.likeCount = likeCount;
        this.followerCount = followerCount;
        this.size = size;
        this.moviesAmount = moviesAmount;
        this.images = images;
    }

    public MoovieListCard(int moovieListId, int userId, String name, String username, String description, int likeCount, int followerCount, int type, int size, int moviesAmount,
                          String images, int currentUserWatchAmount, boolean currentUserHasLiked, boolean currentUserHasFollowed) {
        this.moovieListId = moovieListId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.username = username;
        this.likeCount = likeCount;
        this.followerCount = followerCount;
        this.size = size;
        this.moviesAmount = moviesAmount;
        this.images = images;
        this.currentUserWatchAmount = currentUserWatchAmount;
        this.currentUserHasLiked = currentUserHasLiked;
        this.currentUserHasFollowed = currentUserHasFollowed;
    }


    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getSize() {
        return size;
    }

    public int getMoviesAmount() {
        return moviesAmount;
    }

    public List<String> getImages() {
        List<String> toRet = new ArrayList<>();
        if(this.images!=null){
            String[] aux = this.images.replaceAll("[{}]","").split(",");
            toRet = new ArrayList<>(Arrays.asList(aux));
        }else{
            toRet = new ArrayList<>();
        }
        return toRet;
    }

    public int getCurrentUserWatchAmount() {
        return currentUserWatchAmount;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public boolean isCurrentUserHasFollowed() {
        return currentUserHasFollowed;
    }

    public void setMoovieListId(int moovieListId) {
        this.moovieListId = moovieListId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setMoviesAmount(int moviesAmount) {
        this.moviesAmount = moviesAmount;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setUserStatus(int currentUserWatchAmount, boolean currentUserHasLiked, boolean currentUserHasFollowed){
        this.currentUserWatchAmount = currentUserWatchAmount;
        this.currentUserHasLiked = currentUserHasLiked;
        this.currentUserHasFollowed = currentUserHasFollowed;
    }

    public void setCurrentUserWatchAmount(int currentUserWatchAmount) {
        this.currentUserWatchAmount = currentUserWatchAmount;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public void setCurrentUserHasFollowed(boolean currentUserHasFollowed) {
        this.currentUserHasFollowed = currentUserHasFollowed;
    }
}
