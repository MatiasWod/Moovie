package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;

import java.util.List;

public interface ReportService {

    // STATS

    int getTotalReports();

    int getTypeReports(int type);

    // GENERAL REPORTS
    List<Object> getReports(String contentType);

    List<Object> getReports(String contentType, int pageSize, int pageNumber);

    List<Object> getReports(String contentType, Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    int getReportsCount(String contentType);

    int getReportsCount(String contentType, Integer reportType, Integer resourceId);

    // (Media) Reviews

    List<ReviewReport> getReviewReports();

    List<ReviewReport> getReviewReports(int pageSize, int pageNumber);

    List<ReviewReport> getReviewReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    List<Review> getReportedReviews();

    int getReportedReviewsCount();

    ReviewReport reportReview(int reviewId, int userId, int type);

    ReviewReport getReviewReport(int reportId);

    void resolveReviewReport(int reviewId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();

    List<MoovieListReviewReport> getMoovieListReviewReports(int pageSize, int pageNumber);

    List<MoovieListReviewReport> getMoovieListReviewReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    MoovieListReviewReport getMoovieListReviewReport(int reportId);


    List<MoovieListReview> getReportedMoovieListReviews();

    int getReportedMoovieListReviewsCount();

    MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type);

    void resolveMoovieListReviewReport(int mlrId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();

    List<MoovieListReport> getMoovieListReports(int pageSize, int pageNumber);

    List<MoovieListReport> getMoovieListReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    MoovieListReport getMoovieListReport(int reportId);

    List<MoovieList> getReportedMoovieLists();


    int getReportedMoovieListsCount();

    MoovieListReport reportMoovieList(int moovieListId, int userId, int type);

    void resolveMoovieListReport(int mlId);

    // (Review) Comments

    List<CommentReport> getCommentReports();

    List<CommentReport> getCommentReports(int pageSize, int pageNumber);

    List<CommentReport> getCommentReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    CommentReport getCommentReport(int reportId);

    List<Comment> getReportedComments();

    int getReportedCommentsCount();

    CommentReport reportComment(int commentId, int userId, int type);

    void resolveCommentReport(int commentId);
}
