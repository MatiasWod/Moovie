package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Review.Review;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class ReviewDto {

    private int id;

    private int mediaId;

    private int rating;

    private int reviewLikes;

    private boolean currentUserHasLiked;

    private String mediaPosterPath;

    private String mediaTitle;

    private String reviewContent;

    private Long commentCount;

    private List<Comment> comments;

    private int likes;

    private String username;

    private boolean hasBadge;

    private int totalReports;

    private int spamReports;

    private int hateReports;

    private int privacyReports;

    private int abuseReports;

    private String imageUrl;

    private String url;

    private String userUrl;

    private String mediaUrl;


    public static ReviewDto fromReview(final Review review, UriInfo uriInfo) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.id = review.getReviewId();
        reviewDto.mediaId = review.getMediaId();
        reviewDto.rating = review.getRating();
        reviewDto.reviewLikes = review.getReviewLikes();
        reviewDto.currentUserHasLiked = review.isCurrentUserHasLiked();
        reviewDto.mediaPosterPath = review.getMediaPosterPath();
        reviewDto.mediaTitle = review.getMediaTitle();
        reviewDto.reviewContent = review.getReviewContent();
        reviewDto.commentCount = review.getCommentCount();
        reviewDto.comments = review.getComments();
        reviewDto.likes = review.getReviewLikes();
        reviewDto.username = review.getUser().getUsername();
        reviewDto.hasBadge = review.getUser().isHasBadge();

        reviewDto.totalReports = review.getTotalReports();
        reviewDto.spamReports = review.getSpamReports();
        reviewDto.hateReports = review.getHateReports();
        reviewDto.privacyReports = review.getPrivacyReports();
        reviewDto.abuseReports=review.getAbuseReports();


        reviewDto.url = uriInfo.getBaseUriBuilder().path("/review/{id}").build(review.getReviewId()).toString();
        reviewDto.userUrl = uriInfo.getBaseUriBuilder().path("/users/username/{username}").build(review.getUser().getUsername()).toString();
        reviewDto.imageUrl =  uriInfo.getBaseUriBuilder().path("users/{username}/image").build(review.getUser().getUsername()).toString();
        reviewDto.mediaUrl = uriInfo.getBaseUriBuilder().path("/media/{id}").build(review.getMediaId()).toString();

        return reviewDto;
    }

    public static List<ReviewDto> fromReviewList(final List<Review> reviews, UriInfo uriInfo) {
        return reviews.stream().map(r -> fromReview(r, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public void setReviewLikes(int reviewLikes) {
        this.reviewLikes = reviewLikes;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public String getMediaPosterPath() {
        return mediaPosterPath;
    }

    public void setMediaPosterPath(String mediaPosterPath) {
        this.mediaPosterPath = mediaPosterPath;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHasBadge() {
        return hasBadge;
    }

    public void setHasBadge(boolean hasBadge) {
        this.hasBadge = hasBadge;
    }

    public int getTotalReports() {return totalReports;}

    public void setTotalReports(int totalReports) {this.totalReports = totalReports;}

    public int getSpamReports() {return spamReports;}

    public void setSpamReports(int spamReports) {this.spamReports = spamReports;}

    public int getHateReports() {return hateReports;}

    public void setHateReports(int hateReports) {this.hateReports = hateReports;}

    public int getPrivacyReports() {return privacyReports;}

    public void setPrivacyReports(int privacyReports) {this.privacyReports = privacyReports;}

    public int getAbuseReports() {return abuseReports;}

    public void setAbuseReports(int abuseReports){this.abuseReports=abuseReports;}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }


}
