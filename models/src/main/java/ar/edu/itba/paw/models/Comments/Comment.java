package ar.edu.itba.paw.models.Comments;


import ar.edu.itba.paw.models.Review.Review;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @Column(name = "userid", nullable = false)
    private int userId;

    @Column(name = "reviewId", nullable = false)
    private int reviewId;


    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Transient
    private boolean currentUserHasLiked = false;

    @Transient
    private boolean currentUserHasDisliked = false;

    @Formula("(SELECT COUNT(*) FROM commentlikes cl WHERE cl.commentId = commentId)")
    private int commentLikes;

    @Formula("(SELECT COUNT(*) FROM commentdislikes cl WHERE cl.commentId = commentId)")
    private int commentDislikes;

    @Formula("(SELECT u.username FROM users u WHERE u.userid = userId)")
    private String username;

    @Formula("(SELECT i.image FROM userimages i WHERE i.userid = userId)")
    private byte[] userPfp;

    Comment() {

    }

    public Comment(final int userId, final int reviewId, final String content) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.content = content;
    }


    public boolean isCurrentUserHasDisliked() {
        return currentUserHasDisliked;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public void setCurrentUserHasDisliked(boolean currentUserHasDisliked) {
        this.currentUserHasDisliked = currentUserHasDisliked;
    }

    public byte[] getUserPfp() {
        return userPfp;
    }

    public int getCommentDislikes() {
        return commentDislikes;
    }

    public int getCommentLikes() {
        return commentLikes;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getReviewId() {
        return reviewId;
    }
}
