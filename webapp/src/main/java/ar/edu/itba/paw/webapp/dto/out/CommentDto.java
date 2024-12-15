package ar.edu.itba.paw.webapp.dto.out;


import ar.edu.itba.paw.models.Comments.Comment;

import javax.ws.rs.core.UriInfo;
import java.util.List;

public class CommentDto {

    private int id;

    private int reviewId;

    private int mediaId;

    private String content;

    private boolean currentUserHasLiked;

    private boolean currentUserHasDisliked;

    private int commentLikes;

    private int commentDislikes;

    private int totalReports;

    private int spamReports;

    private int hateReports;

    private int abuseReports;

    private String url;

    private String userUrl;

    private String reviewUrl;

    //TODO IMPLEMENTAR ESTE URL
    private String reportsUrl;

    public static CommentDto fromComment(final Comment comment, UriInfo uriInfo) {
        CommentDto commentDto = new CommentDto();
        commentDto.id = comment.getCommentId();
        commentDto.reviewId = comment.getReviewId();
        commentDto.mediaId = comment.getMediaId();
        commentDto.content = comment.getContent();
        commentDto.currentUserHasLiked = comment.isCurrentUserHasLiked();
        commentDto.currentUserHasDisliked = comment.isCurrentUserHasDisliked();
        commentDto.commentLikes = comment.getCommentLikes();
        commentDto.commentDislikes = comment.getCommentDislikes();
        commentDto.totalReports = comment.getTotalReports();
        commentDto.spamReports = comment.getSpamReports();
        commentDto.hateReports = comment.getHateReports();
        commentDto.abuseReports = comment.getAbuseReports();

        commentDto.url = uriInfo.getBaseUriBuilder().path("/comment/{id}").build(comment.getCommentId()).toString();
        commentDto.reviewUrl = uriInfo.getBaseUriBuilder().path("/review/{id}").build(comment.getReviewId()).toString();
        //TODO IMPLEMENTAR reportsURL
        commentDto.reportsUrl = "";
        commentDto.userUrl = uriInfo.getBaseUriBuilder().path("/user/{id}").build(comment.getUsername()).toString();

        return commentDto;

    }

    public static List<CommentDto> fromCommentList(final List<Comment> commentList, final UriInfo uriInfo) {
        return commentList.stream().map(r -> fromComment(r, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCurrentUserHasLiked() {
        return currentUserHasLiked;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked) {
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public boolean isCurrentUserHasDisliked() {
        return currentUserHasDisliked;
    }

    public void setCurrentUserHasDisliked(boolean currentUserHasDisliked) {
        this.currentUserHasDisliked = currentUserHasDisliked;
    }

    public int getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(int commentLikes) {
        this.commentLikes = commentLikes;
    }

    public int getCommentDislikes() {
        return commentDislikes;
    }

    public void setCommentDislikes(int commentDislikes) {
        this.commentDislikes = commentDislikes;
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

    public int getAbuseReports() {
        return abuseReports;
    }

    public void setAbuseReports(int abuseReports) {
        this.abuseReports = abuseReports;
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

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getReportsUrl() {
        return reportsUrl;
    }

    public void setReportsUrl(String reportsUrl) {
        this.reportsUrl = reportsUrl;
    }

}
