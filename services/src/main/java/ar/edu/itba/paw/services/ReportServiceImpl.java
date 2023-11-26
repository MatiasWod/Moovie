package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportDao reportDao;

    @Override
    public List<ReviewReport> getReviewReports() {

        return reportDao.getReviewReports();
    }

    @Override
    public List<Review> getReportedReviews() {
        return reportDao.getReportedReviews();
    }

    @Override
    @Transactional
    public void reportReview(int reviewId, int userId, int type, String content) {
        reportDao.reportReview(reviewId, userId, type, content);
    }

    @Override
    @Transactional
    public void resolveReviewReport(int reportId) {
        reportDao.resolveReviewReport(reportId);
    }

    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        return reportDao.getMoovieListReviewReports();
    }

    @Override
    public List<MoovieListReview> getReportedMoovieListReviews() {
        return reportDao.getReportedMoovieListReviews();
    }

    @Override
    @Transactional
    public void reportMoovieListReview(int moovieListReviewId, int userId, int type, String content) {
        reportDao.reportMoovieListReview(moovieListReviewId, userId, type, content);
    }

    @Override
    @Transactional
    public void resolveMoovieListReviewReport(int reportId) {
        reportDao.resolveMoovieListReviewReport(reportId);
    }

    @Override
    public List<MoovieListReport> getMoovieListReports() {

        return reportDao.getMoovieListReports();
    }

    @Override
    public List<MoovieList> getReportedMoovieLists() {
        return reportDao.getReportedMoovieLists();
    }

    @Override
    @Transactional
    public void reportMoovieList(int moovieListId, int userId, int type, String content) {
        reportDao.reportMoovieList(moovieListId, userId, type, content);
    }

    @Override
    @Transactional
    public void resolveMoovieListReport(int reportId) {
        reportDao.resolveMoovieListReport(reportId);
    }

    @Override
    public List<CommentReport> getCommentReports() {

        return reportDao.getCommentReports();
    }

    @Override
    public List<Comment> getReportedComments() {
        return reportDao.getReportedComments();
    }

    @Override
    @Transactional
    public void reportComment(int commentId, int userId, int type, String content) {
        reportDao.reportComment(commentId, userId, type, content);
    }

    @Override
    @Transactional
    public void resolveCommentReport(int reportId) {
        reportDao.resolveCommentReport(reportId);
    }
}
