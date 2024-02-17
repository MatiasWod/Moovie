package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.ReviewsLikes;
import ar.edu.itba.paw.models.User.User;

import java.util.List;

public class ReviewDto {

    private int mediaId;

    private int rating;

    private int reviewLikes;

    private boolean currentUserHasLiked;

    private String mediaPosterPath;

    private String mediaTitle;

    private String reviewContent;

    private Long commentCount;

    private List<Comment> comments;

    private List<ReviewReport> reports;

    private List<ReviewsLikes> likes;

    private User user;

    public ReviewDto(int mediaId, int rating, int reviewLikes, boolean currentUserHasLiked, String mediaPosterPath, String mediaTitle, String reviewContent, Long commentCount, List<Comment> comments, List<ReviewReport> reports, List<ReviewsLikes> likes, User user) {
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewLikes = reviewLikes;
        this.currentUserHasLiked = currentUserHasLiked;
        this.mediaPosterPath = mediaPosterPath;
        this.mediaTitle = mediaTitle;
        this.reviewContent = reviewContent;
        this.commentCount = commentCount;
        this.comments = comments;
        this.reports = reports;
        this.likes = likes;
        this.user = user;
    }


}
