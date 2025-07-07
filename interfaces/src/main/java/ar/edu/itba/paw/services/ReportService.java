package ar.edu.itba.paw.services;

import java.util.List;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;

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

    List<Review> getReportedReviews(int pageSize, int pageNumber);

    int getReportedReviewsCount();

    ReviewReport reportReview(int reviewId, int userId, int type);

    ReviewReport getReviewReport(int reportId);

    void resolveReviewReport(int reportId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();

    List<MoovieListReviewReport> getMoovieListReviewReports(int pageSize, int pageNumber);

    List<MoovieListReviewReport> getMoovieListReviewReports(Integer reportType, Integer resourceId, int pageSize,
            int pageNumber);

    MoovieListReviewReport getMoovieListReviewReport(int reportId);

    List<MoovieListReview> getReportedMoovieListReviews(int pageSize, int pageNumber);

    int getReportedMoovieListReviewsCount();

    MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type);

    void resolveMoovieListReviewReport(int reportId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();

    List<MoovieListReport> getMoovieListReports(int pageSize, int pageNumber);

    List<MoovieListReport> getMoovieListReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    MoovieListReport getMoovieListReport(int reportId);

    List<MoovieListCard> getReportedMoovieLists(int pageSize, int pageNumber, int userId);

    int getReportedMoovieListsCount();

    MoovieListReport reportMoovieList(int moovieListId, int userId, int type);

    void resolveMoovieListReport(int reportId);

    // (Review) Comments

    List<CommentReport> getCommentReports();

    List<CommentReport> getCommentReports(int pageSize, int pageNumber);

    List<CommentReport> getCommentReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber);

    CommentReport getCommentReport(int reportId);

    List<Comment> getReportedComments(int pageSize, int pageNumber);

    int getReportedCommentsCount();

    CommentReport reportComment(int commentId, int userId, int type);

    void resolveCommentReport(int reportId);
}
