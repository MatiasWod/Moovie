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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private ReportDao reportDao;

    @Transactional(readOnly = true)
    @Override
    public int getTotalReports() {
        return reportDao.getTotalReports();
    }

    @Transactional(readOnly = true)
    @Override
    public int getTypeReports(int type) {
        return reportDao.getTypeReports(type);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewReport> getReviewReports() {

        return reportDao.getReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getReportedReviews() {

        return reportDao.getReportedReviews();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedReviewsCount() {
        return reportDao.getReportedReviewsCount();
    }

    @Transactional
    @Override
    public void reportReview(int reviewId, int userId, int type, String content) {
        LOGGER.info("reportReview insert");
        reportDao.reportReview(reviewId, userId, type, content);
    }

    @Transactional
    @Override
    public void resolveReviewReport(int reportId) {

        reportDao.resolveReviewReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        return reportDao.getMoovieListReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getReportedMoovieListReviews() {

        return reportDao.getReportedMoovieListReviews();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListReviewsCount() {
        return reportDao.getReportedMoovieListReviewsCount();
    }

    @Transactional
    @Override
    public void reportMoovieListReview(int moovieListReviewId, int userId, int type, String content) {
        reportDao.reportMoovieListReview(moovieListReviewId, userId, type, content);
    }

    @Transactional
    @Override
    public void resolveMoovieListReviewReport(int reportId) {

        reportDao.resolveMoovieListReviewReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReport> getMoovieListReports() {

        return reportDao.getMoovieListReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieList> getReportedMoovieLists() {
        return reportDao.getReportedMoovieLists();
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListsCount() {
        return reportDao.getReportedMoovieListsCount();
    }

    @Transactional
    @Override
    public void reportMoovieList(int moovieListId, int userId, int type, String content) {
        reportDao.reportMoovieList(moovieListId, userId, type, content);
    }

    @Transactional
    @Override
    public void resolveMoovieListReport(int reportId) {

        reportDao.resolveMoovieListReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentReport> getCommentReports() {

        return reportDao.getCommentReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getReportedComments() {
        return reportDao.getReportedComments();
    }

    @Override
    public int getReportedCommentsCount() {
        return reportDao.getReportedCommentsCount();
    }

    @Transactional
    @Override
    public void reportComment(int commentId, int userId, int type, String content) {
        reportDao.reportComment(commentId, userId, type, content);
    }

    @Transactional
    @Override
    public void resolveCommentReport(int reportId) {

        reportDao.resolveCommentReport(reportId);
    }
}
