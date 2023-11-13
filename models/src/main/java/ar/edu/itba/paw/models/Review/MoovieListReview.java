package ar.edu.itba.paw.models.Review;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "moovieListsReviews")
public class MoovieListReview {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielistsreviews_moovielistreviewid_seq")
    @SequenceGenerator(sequenceName = "moovielistsreviews_moovielistreviewid_seq", name = "moovielistsreviews_moovielistreviewid_seq", allocationSize = 1)
    @Column(name = "moovieListReviewId")
    private int moovieListReviewId;

    @Column(name = "userid", nullable = false)
    private int userId;
    @Formula("(SELECT u.username FROM users u WHERE u.userid = userId)")
    private String username;
    @Column(nullable = false)
    private int moovieListId;

    @Formula("(SELECT COUNT(*) FROM moovieListsReviewsLikes WHERE moovieListsReviewsLikes.moovieListReviewId = moovieListReviewId)")
    private int reviewLikes;
    @Transient
    private boolean currentUserHasLiked = false;
    @Formula("( SELECT ARRAY_AGG(m.posterPath) FROM moovielistscontent mlc INNER JOIN media m ON mlc.mediaId = m.mediaId WHERE mlc.moovielistId = moovieListId )")
    private String moovieListImages;

    @Formula("(SELECT m.name FROM moovieLists m WHERE m.moovielistid = moovielistid)")
    private String moovieListTitle;

    @Column(columnDefinition = "TEXT")
    private String reviewContent;

    //hibernate
    MoovieListReview() {
    }

    public MoovieListReview(int userId, int moovieListId, String reviewContent) {
        this.userId = userId;
        this.moovieListId = moovieListId;
        this.reviewContent = reviewContent;
    }

    public void setMoovieListReviewId(int moovieListReviewId) {
        this.moovieListReviewId = moovieListReviewId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMoovieListId(int mediaId) {
        this.moovieListId = mediaId;
    }


    public void setReviewLikes(int reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public void setMoovieListImages(String moovieListImages) {
        this.moovieListImages = moovieListImages;
    }

    public void setMoovieListTitle(String moovieListTitle) {
        this.moovieListTitle = moovieListTitle;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getMoovieListReviewId() {
        return moovieListReviewId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public List<String> getMoovieListImages() {
        List<String> toRet = new ArrayList<>();
        if(this.moovieListImages!=null){
            String[] aux = this.moovieListImages.replaceAll("[{}]","").split(",");
            toRet = new ArrayList<>(Arrays.asList(aux));
            if (toRet.size() > 4) {
                toRet = toRet.subList(0, 4);
            }
        }else{
            toRet = new ArrayList<>();
        }
        return toRet;
    }

    public String getMoovieListTitle() {
        return moovieListTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
