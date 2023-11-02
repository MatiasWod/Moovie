package ar.edu.itba.paw.models.Review;

import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_reviewid_seq")
    @SequenceGenerator(sequenceName = "review_reviewid_seq", name = "review_reviewid_seq", allocationSize = 1)
    @Column(name = "reviewid")
    private int reviewId;

    @Column(name = "userid", nullable = false)
    private int userId;
    @Formula("(SELECT u.username FROM users u WHERE u.userid = userId)")
    private String username;
    @Column(nullable = false)
    private int mediaId;
    @Column(nullable = false, columnDefinition = "SMALLINT")
    private int rating;

    @Formula("(SELECT COUNT(*) FROM reviewslikes WHERE reviewslikes.reviewid = reviewid)")
    private int reviewLikes;
    @Transient
    private boolean currentUserHasLiked = false;
    @Formula("(SELECT m.posterPath FROM media m WHERE m.mediaId = mediaId)")
    private String mediaPosterPath;

    @Formula("(SELECT m.name FROM media m WHERE m.mediaId = mediaId)")
    private String mediaTitle;

    @Column(columnDefinition = "TEXT")
    private String reviewContent;

    //hibernate
    Review() {
    }

    public Review(int userId, int mediaId, int rating,String reviewContent) {
        this.userId = userId;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getRating() {
        return rating;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public String getMediaPosterPath() {
        return mediaPosterPath;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setHasLiked(boolean b) {
        this.currentUserHasLiked = b;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void raiseLikeAmount() {
        this.reviewLikes++;
    }

    public void lowerLikeAmount() {
        this.reviewLikes--;
    }
}
