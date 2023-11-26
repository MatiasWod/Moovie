package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;

import java.util.List;

public interface ReportService {

    // STATS

    int getTotalReports();
    int getTypeReports(int type);

    // (Media) Reviews

    List<ReviewReport> getReviewReports();
    List<Review> getReportedReviews();

    int getReportedReviewsCount();
    void reportReview(int reviewId, int userId, int type, String content);
    void resolveReviewReport(int reportId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();
    List<MoovieListReview> getReportedMoovieListReviews();
    int getReportedMoovieListReviewsCount();
    void reportMoovieListReview(int moovieListReviewId, int userId, int type, String content);
    void resolveMoovieListReviewReport(int reportId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();
    List<MoovieList> getReportedMoovieLists();
    int getReportedMoovieListsCount();
    void reportMoovieList(int moovieListId, int userId, int type, String content);
    void resolveMoovieListReport(int reportId);

    // (Review) Comments

    List<CommentReport> getCommentReports();
    List<Comment> getReportedComments();
    int getReportedCommentsCount();
    void reportComment(int commentId, int userId, int type, String content);

    void resolveCommentReport(int reportId);
}
