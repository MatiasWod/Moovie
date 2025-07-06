package ar.edu.itba.paw.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReportDao;

@Service
public class ReportServiceImpl implements ReportService {

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
    public List<Object> getReports(String contentType) {
        List<Object> reports = new ArrayList<>();

        if (contentType == null || contentType.isEmpty()) {
            // If no content type specified, fetch all reports
            reports.addAll(reportDao.getCommentReports());
            reports.addAll(reportDao.getReviewReports());
            reports.addAll(reportDao.getMoovieListReports());
            reports.addAll(reportDao.getMoovieListReviewReports());
        } else {
            // Fetch reports based on the specified content type
            switch (contentType.toLowerCase()) {
                case "comment":
                    reports.addAll(reportDao.getCommentReports());
                    break;
                case "review":
                    reports.addAll(reportDao.getReviewReports());
                    break;
                case "moovielist":
                    reports.addAll(reportDao.getMoovieListReports());
                    break;
                case "moovielistreview":
                    reports.addAll(reportDao.getMoovieListReviewReports());
                    break;
                default:
                    // Return empty list for invalid content types
                    break;
            }
        }

        return reports;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object> getReports(String contentType, int pageSize, int pageNumber) {
        List<Object> reports = new ArrayList<>();

        if (contentType == null || contentType.isEmpty()) {
            // If no content type specified, fetch all report content types
            reports.addAll(reportDao.getReports(pageSize, pageNumber));
        } else {
            // Fetch reports based on the specified content type
            switch (contentType.toLowerCase()) {
                case "comment":
                    reports.addAll(reportDao.getCommentReports(pageSize, pageNumber));
                    break;
                case "review":
                    reports.addAll(reportDao.getReviewReports(pageSize, pageNumber));
                    break;
                case "moovielist":
                    reports.addAll(reportDao.getMoovieListReports(pageSize, pageNumber));
                    break;
                case "moovielistreview":
                    reports.addAll(reportDao.getMoovieListReviewReports(pageSize, pageNumber));
                    break;
                default:
                    // Return empty list for invalid content types
                    break;
            }
        }

        return reports;
    }

    @Override
    public int getReportsCount(String contentType) {

        if (contentType == null || contentType.isEmpty()) {
            // If no content type specified, fetch all report content types
            return reportDao.getTotalReports();
        } else {
            return reportDao.getReportsCount(contentType);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewReport> getReviewReports() {

        return reportDao.getReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewReport> getReviewReports(int pageSize, int pageNumber) {
        return reportDao.getReviewReports(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ReviewReport> getReviewReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber) {
        return reportDao.getReviewReports(reportType, resourceId, pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public ReviewReport getReviewReport(int reportId) {
        return reportDao.getReviewReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getReportedReviews(int pageSize, int pageNumber) {

        return reportDao.getReportedReviews(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedReviewsCount() {
        return reportDao.getReportedReviewsCount();
    }

    @Transactional
    @Override
    public ReviewReport reportReview(int reviewId, int userId, int type) {
        LOGGER.info("reportReview insert");
        return reportDao.reportReview(reviewId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveReviewReport(int reviewId) {

        reportDao.resolveReviewReport(reviewId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        return reportDao.getMoovieListReviewReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports(int pageSize, int pageNumber) {
        return reportDao.getMoovieListReviewReports(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports(Integer reportType, Integer resourceId, int pageSize,
            int pageNumber) {
        if (reportType == null && resourceId == null) {
            return reportDao.getMoovieListReviewReports(pageSize, pageNumber);
        } else {
            return reportDao.getMoovieListReviewReports(reportType, resourceId, pageSize, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListReviewReport getMoovieListReviewReport(int reportId) {
        return reportDao.getMoovieListReviewReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReview> getReportedMoovieListReviews(int pageSize, int pageNumber) {
        return reportDao.getReportedMoovieListReviews(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListReviewsCount() {
        return reportDao.getReportedMoovieListReviewsCount();
    }

    @Transactional
    @Override
    public MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type) {
        return reportDao.reportMoovieListReview(moovieListReviewId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveMoovieListReviewReport(int mlrId) {

        reportDao.resolveMoovieListReviewReport(mlrId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReport> getMoovieListReports() {

        return reportDao.getMoovieListReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getReportedMoovieLists(int pageSize, int pageNumber, int userId) {
        return reportDao.getReportedMoovieLists(pageSize, pageNumber, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedMoovieListsCount() {
        return reportDao.getReportedMoovieListsCount();
    }

    @Transactional
    @Override
    public MoovieListReport reportMoovieList(int moovieListId, int userId, int type) {
        return reportDao.reportMoovieList(moovieListId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveMoovieListReport(int mlId) {

        reportDao.resolveMoovieListReport(mlId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentReport> getCommentReports() {

        return reportDao.getCommentReports();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getReportedComments(int pageSize, int pageNumber) {
        return reportDao.getReportedComments(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentReport> getCommentReports(int pageSize, int pageNumber) {
        return reportDao.getCommentReports(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentReport> getCommentReports(Integer reportType, Integer resourceId, int pageSize, int pageNumber) {
        if (reportType == null && resourceId == null) {
            return reportDao.getCommentReports(pageSize, pageNumber);
        } else {
            return reportDao.getCommentReports(reportType, resourceId, pageSize, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReport> getMoovieListReports(int pageSize, int pageNumber) {
        return reportDao.getMoovieListReports(pageSize, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListReport> getMoovieListReports(Integer reportType, Integer resourceId, int pageSize,
            int pageNumber) {
        if (reportType == null && resourceId == null) {
            return reportDao.getMoovieListReports(pageSize, pageNumber);
        } else {
            return reportDao.getMoovieListReports(reportType, resourceId, pageSize, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public MoovieListReport getMoovieListReport(int reportId) {
        return reportDao.getMoovieListReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentReport getCommentReport(int reportId) {
        return reportDao.getCommentReport(reportId);
    }

    @Transactional(readOnly = true)
    @Override
    public int getReportedCommentsCount() {
        return reportDao.getReportedCommentsCount();
    }

    @Transactional
    @Override
    public CommentReport reportComment(int commentId, int userId, int type) {
        return reportDao.reportComment(commentId, userId, type, null);
    }

    @Transactional
    @Override
    public void resolveCommentReport(int commentId) {

        reportDao.resolveCommentReport(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Object> getReports(String contentType, Integer reportType, Integer resourceId, int pageSize,
            int pageNumber) {
        return reportDao.getReports(contentType, reportType, resourceId, pageSize, pageNumber);
    }

    @Override
    public int getReportsCount(String contentType, Integer reportType, Integer resourceId) {
        return reportDao.getReportsCount(contentType, reportType, resourceId);
    }
}
