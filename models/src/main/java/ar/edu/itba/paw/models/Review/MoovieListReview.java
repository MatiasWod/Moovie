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

    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.moovieListReviewId = :moovieListReviewId)")
    private int totalReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 3 AND rr.moovieListReviewId = :moovieListReviewId)")
    private int spamReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 0 AND rr.moovieListReviewId = :moovieListReviewId)")
    private int hateReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 2 AND rr.moovieListReviewId = :moovieListReviewId)")
    private int privacyReports;
    @Formula("(SELECT COUNT(*) FROM reportsMoovieListReviews rr WHERE rr.type = 1 AND rr.moovieListReviewId = :moovieListReviewId)")
    private int abuseReports;

    //hibernate
    MoovieListReview() {
    }

    public MoovieListReview(int userId, int moovieListId, String reviewContent) {
        this.userId = userId;
        this.moovieListId = moovieListId;
        this.reviewContent = reviewContent;
    }

    public MoovieListReview(int moovieListReviewId, int userId, String username, int moovieListId, int reviewLikes, boolean currentUserHasLiked, String moovieListImages, String moovieListTitle, String reviewContent) {
        this.moovieListReviewId = moovieListReviewId;
        this.userId = userId;
        this.username = username;
        this.moovieListId = moovieListId;
        this.reviewLikes = reviewLikes;
        this.currentUserHasLiked = currentUserHasLiked;
        this.moovieListImages = moovieListImages;
        this.moovieListTitle = moovieListTitle;
        this.reviewContent = reviewContent;
    }

    public MoovieListReview(int moovieListReviewId, int userId, String username, int moovieListId, int reviewLikes, String moovieListImages, String moovieListTitle, String reviewContent) {
        this.moovieListReviewId = moovieListReviewId;
        this.userId = userId;
        this.username = username;
        this.moovieListId = moovieListId;
        this.reviewLikes = reviewLikes;
        this.moovieListImages = moovieListImages;
        this.moovieListTitle = moovieListTitle;
        this.reviewContent = reviewContent;
    }

    public MoovieListReview(MoovieListReview m, int currentUserHasLiked){
        this.moovieListReviewId = m.moovieListReviewId;
        this.userId = m.userId;
        this.username = m.username;
        this.moovieListId = m.moovieListId;
        this.reviewLikes = m.reviewLikes;
        this.currentUserHasLiked = currentUserHasLiked == 1;
        this.moovieListImages = m.moovieListImages;
        this.moovieListTitle = m.moovieListTitle;
        this.reviewContent = m.reviewContent;
    }


    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }

    public int getSpamReports() {
        return spamReports;
    }

    public void setSpamReports(int spamReports) {
        this.spamReports = spamReports;
    }

    public int getHateReports() {
        return hateReports;
    }

    public void setHateReports(int hateReports) {
        this.hateReports = hateReports;
    }

    public int getPrivacyReports() {
        return privacyReports;
    }

    public void setPrivacyReports(int privacyReports) {
        this.privacyReports = privacyReports;
    }

    public int getAbuseReports() {
        return abuseReports;
    }

    public void setAbuseReports(int abuseReports) {
        this.abuseReports = abuseReports;
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
