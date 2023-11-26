package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;

import java.util.List;

public interface ReportService {

    // (Media) Reviews

    List<ReviewReport> getReviewReports();
    void reportReview(int reviewId, int userId, String type, String content);
    void resolveReviewReport(int reportId);

    // MoovieListReviews

    List<MoovieListReviewReport> getMoovieListReviewReports();
    void reportMoovieListReview(int moovieListReviewId, int userId, String type, String content);
    void resolveMoovieListReviewReport(int reportId);

    // MoovieLists

    List<MoovieListReport> getMoovieListReports();
    void reportMoovieList(int moovieListId, int userId, String type, String content);
    void resolveMoovieListReport(int reportId);

    // (Review) Comments

    List<CommentReport> getCommentReports();
    void reportComment(int commentId, int userId, String type, String content);

    void resolveCommentReport(int reportId);
}
