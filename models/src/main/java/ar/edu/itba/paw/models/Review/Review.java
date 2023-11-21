package ar.edu.itba.paw.models.Review;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

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

    @Formula("(SELECT COUNT(*) FROM comments c WHERE c.reviewid = reviewId)")
    private Long commentCount;

    @Transient
    private List<Comment> comments;

    //hibernate
    Review() {
    }

    public Review(int userId, int mediaId, int rating, String reviewContent) {
        this.userId = userId;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewContent = reviewContent;
    }

    public Review(int reviewId, int userId, String username, int mediaId, int rating, int reviewLikes, String mediaPosterPath, String mediaTitle, String reviewContent, Long commentCount, List<Comment> comments) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.username = username;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewLikes = reviewLikes;
        this.mediaPosterPath = mediaPosterPath;
        this.mediaTitle = mediaTitle;
        this.reviewContent = reviewContent;
        this.commentCount = commentCount;
        this.comments = comments;
    }


    public Review(Review r, int currentUserHasLiked){
        this.reviewId = r.reviewId;
        this.userId = r.userId;
        this.username = r.username;
        this.mediaId = r.mediaId;
        this.rating = r.rating;
        this.reviewLikes = r.reviewLikes;
        this.mediaPosterPath = r.mediaPosterPath;
        this.mediaTitle = r.mediaTitle;
        this.reviewContent = r.reviewContent;
        this.commentCount = r.commentCount;
        this.comments = r.comments;
        r.currentUserHasLiked = currentUserHasLiked==1;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Long getCommentCount() {
        return commentCount;
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
