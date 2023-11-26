package ar.edu.itba.paw.models.Comments;


import ar.edu.itba.paw.models.Review.Review;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

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

    @Formula("CASE WHEN EXISTS (SELECT 1 FROM userimages ui WHERE ui.userid = userId) THEN 1 ELSE 0 END")
    private boolean hasPfp;


    @Formula("SELECT COUNT(*) FROM reportscomments rc WHERE rc.commentid = :commentId")
    private int totalReports;
    @Formula("SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 3 AND rc.commentid = :commentId")
    private int spamReports;
    @Formula("SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 0 AND rc.commentid = :commentId")
    private int hateReports;
    @Formula("SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 2 AND rc.commentid = :commentId")
    private int privacyReports;
    @Formula("SELECT COUNT(*) FROM reportscomments rc WHERE rc.type = 1 AND rc.commentid = :commentId")
    private int abuseReports;

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

    public boolean getUserPfp() {
        return hasPfp;
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
