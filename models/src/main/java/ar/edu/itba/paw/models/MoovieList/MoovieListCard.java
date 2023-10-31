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

    @Transient
    private boolean currentUserHasLiked;

    @Formula("(SELECT COUNT(*) FROM moovielistsfollows mll WHERE mll.moovielistid = moovielistid)")
    private int followerCount;

    @Transient
    private boolean currentUserHasFollowed;

    @Formula("(SELECT COUNT(*) FROM moovieListsContent mlc WHERE mlc.moovieListId = moovieListId)")
    private int size;

    @Formula("(SELECT COUNT(*) FROM moovielistsContent mlc INNER JOIN media m ON mlc.mediaid = m.mediaid WHERE m.type = false AND mlc.moovieListid = moovieListId)")
    private int moviesAmount;

    @Transient
    private int currentUserWatchAmount;

    @Transient
    private List<String> images;

    public MoovieListCard(){}

    public MoovieListCard(int moovieListId, String name,  int userid , String username, String description, int likeCount,  boolean currentUserHasLiked, int followerCount, boolean currentUserHasFollowed, int type, int size, int moviesAmount, int currentUserWatchAmount, String images) {
        this.moovieListId = moovieListId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.username = username;
        this.likeCount = likeCount;
        this.currentUserHasLiked = currentUserHasLiked;
        this.followerCount = followerCount;
        this.currentUserHasFollowed = currentUserHasFollowed;
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

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public boolean isCurrentUserHasFollowed() {
        return currentUserHasFollowed;
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
