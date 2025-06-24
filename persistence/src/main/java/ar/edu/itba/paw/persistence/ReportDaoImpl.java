package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.ConflictException;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Primary
@Repository
public class ReportDaoImpl implements ReportDao {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportDaoImpl.class);

    @Override
    public int getTotalReports() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM reportsreviews) + " +
                "(SELECT COUNT(*) FROM reportsmoovielistreviews) + " +
                "(SELECT COUNT(*) FROM reportsmoovielists) + " +
                "(SELECT COUNT(*) FROM reportscomments) AS total_count";

        BigInteger toReturn = (BigInteger) em.createNativeQuery(sql).getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public int getTypeReports(int type) {
        final String sql = "SELECT " +
                "(SELECT COUNT(*) FROM reportsreviews r WHERE r.type = :type) + " +
                "(SELECT COUNT(*) FROM reportsmoovielistreviews r WHERE r.type = :type) + " +
                "(SELECT COUNT(*) FROM reportsmoovielists r WHERE r.type = :type) + " +
                "(SELECT COUNT(*) FROM reportscomments r WHERE r.type = :type) AS total_count";

        BigInteger toReturn = (BigInteger) em.createNativeQuery(sql)
                .setParameter("type", type)
                .getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public List<Object> getReports(int pageSize, int pageNumber) {

        /*
         * ------------------------------------------------------------------
         * 1) Build a single native query that UNION-ALLs the four report
         * tables, keeps the primary-key (reportid) and a string flag
         * telling us which concrete entity it belongs to, orders everything
         * by report_date (newest first) and applies pagination with the
         * requested limit / offset.
         * ------------------------------------------------------------------
         */
        final String sql = "(SELECT reportid, 'REVIEW'       AS src, report_date FROM reportsreviews)              " +
                "UNION ALL                                                                                 " +
                "(SELECT reportid, 'MLREVIEW'    AS src, report_date FROM reportsmoovielistreviews)       " +
                "UNION ALL                                                                                 " +
                "(SELECT reportid, 'MOOVIELIST'  AS src, report_date FROM reportsmoovielists)             " +
                "UNION ALL                                                                                 " +
                "(SELECT reportid, 'COMMENT'     AS src, report_date FROM reportscomments)                " +
                "ORDER BY report_date DESC                                                                 " +
                "LIMIT :limit OFFSET :offset";

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("limit", pageSize)
                .setParameter("offset", offset)
                .getResultList();

        /*
         * ------------------------------------------------------------------
         * 2) Translate every row to the proper JPA entity so the caller
         * receives a strongly-typed list it can safely cast/instanceof.
         * ------------------------------------------------------------------
         */
        List<Object> reports = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            Number idNumber = (Number) row[0];
            int id = idNumber.intValue();
            String kind = ((String) row[1]).toUpperCase();

            switch (kind) {
                case "REVIEW":
                    reports.add(em.find(ReviewReport.class, id));
                    break;
                case "MLREVIEW":
                    reports.add(em.find(MoovieListReviewReport.class, id));
                    break;
                case "MOOVIELIST":
                    reports.add(em.find(MoovieListReport.class, id));
                    break;
                case "COMMENT":
                    reports.add(em.find(CommentReport.class, id));
                    break;
                default:
                    break;
            }
        }

        return reports;
    }

    @Override
    public int getReportsCount(String type) {
        String selectClause;
        switch (type.toLowerCase()) {
            case "review":
                selectClause = "(SELECT COUNT(*) FROM reportsreviews)";
                break;
            case "comment":
                selectClause = "(SELECT COUNT(*) FROM reportscomments)";
                break;
            case "moovielist":
                selectClause = "(SELECT COUNT(*) FROM reportsmoovielists)";
                break;
            case "moovielistreview":
                selectClause = "(SELECT COUNT(*) FROM reportsmoovielistreviews)";
                break;
            default:
                selectClause = "SELECT 0";
        }

        BigInteger toReturn = (BigInteger) em.createNativeQuery(selectClause).getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public List<ReviewReport> getReviewReports() {

        String sql = "SELECT c FROM ReviewReport c";

        TypedQuery<ReviewReport> query = em.createQuery(sql, ReviewReport.class);

        return query.getResultList();
    }

    @Override
    public List<ReviewReport> getReviewReports(int pageSize, int pageNumber) {

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        String sql = "SELECT c FROM ReviewReport c ORDER BY c.report_date DESC";

        TypedQuery<ReviewReport> query = em.createQuery(sql, ReviewReport.class)
                .setMaxResults(pageSize)
                .setFirstResult(offset);

        return query.getResultList();
    }

    @Override
    public List<Review> getReportedReviews() {
        String sql = "SELECT r FROM Review r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<Review> query = em.createQuery(sql, Review.class);

        return query.getResultList();
    }

    @Override
    public int getReportedReviewsCount() {
        String sql = "SELECT r FROM Review r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<Review> query = em.createQuery(sql, Review.class);

        return query.getResultList().size();
    }

    @Override
    public ReviewReport reportReview(int reviewId, int userId, int type, String content) {
        Review review = em.find(Review.class, reviewId);
        User user = em.find(User.class, userId);

        List<ReviewReport> existingReports = em.createQuery(
                "SELECT r FROM ReviewReport r WHERE r.review = :review AND r.reportedBy = :user",
                ReviewReport.class)
                .setParameter("review", review)
                .setParameter("user", user)
                .getResultList();

        if (!existingReports.isEmpty()) {
            throw new ConflictException("User already reported this review");
        }

        ReviewReport report = new ReviewReport(type, content, user, review);
        em.persist(report);
        return report;
    }

    @Override
    public void resolveReviewReport(int reviewId) {
        String sql = "SELECT r FROM ReviewReport r WHERE r.review.reviewId = :reviewId";
        List<ReviewReport> toRemove = em.createQuery(sql, ReviewReport.class)
                .setParameter("reviewId", reviewId)
                .getResultList();

        for (ReviewReport report : toRemove) {
            em.remove(report);
        }
    }

    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        String sql = "SELECT c FROM MoovieListReviewReport c";

        TypedQuery<MoovieListReviewReport> query = em.createQuery(sql, MoovieListReviewReport.class);

        return query.getResultList();
    }

    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports(int pageSize, int pageNumber) {

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        String sql = "SELECT c FROM MoovieListReviewReport c ORDER BY c.report_date DESC";

        TypedQuery<MoovieListReviewReport> query = em.createQuery(sql, MoovieListReviewReport.class)
                .setMaxResults(pageSize)
                .setFirstResult(offset);

        return query.getResultList();
    }

    @Override
    public List<MoovieListReview> getReportedMoovieListReviews() {

        String sql = "SELECT r FROM MoovieListReview r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<MoovieListReview> query = em.createQuery(sql, MoovieListReview.class);

        return query.getResultList();
    }

    @Override
    public int getReportedMoovieListReviewsCount() {
        String sql = "SELECT r FROM MoovieListReview r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<MoovieListReview> query = em.createQuery(sql, MoovieListReview.class);

        return query.getResultList().size();
    }

    @Override
    public MoovieListReviewReport reportMoovieListReview(int moovieListReviewId, int userId, int type, String content) {
        MoovieListReview review = em.find(MoovieListReview.class, moovieListReviewId);
        User user = em.find(User.class, userId);

        List<MoovieListReviewReport> existingReports = em.createQuery(
                "SELECT m FROM MoovieListReviewReport m WHERE m.moovieListReview = :moovieListReview AND m.reportedBy = :user",
                MoovieListReviewReport.class)
                .setParameter("moovieListReview", review)
                .setParameter("user", user)
                .getResultList();

        if (!existingReports.isEmpty()) {
            throw new ConflictException("User already reported this moovielistReview");
        }

        MoovieListReviewReport report = new MoovieListReviewReport(type, content, user, review);
        em.persist(report);
        return report;
    }

    @Override
    public void resolveMoovieListReviewReport(int moovieListReviewId) {
        String sql = "SELECT r FROM MoovieListReviewReport r WHERE r.moovieListReview.moovieListReviewId = :moovieListReviewId";
        List<MoovieListReviewReport> toRemove = em.createQuery(sql, MoovieListReviewReport.class)
                .setParameter("moovieListReviewId", moovieListReviewId)
                .getResultList();

        for (MoovieListReviewReport report : toRemove) {
            em.remove(report);
        }
    }

    @Override
    public List<MoovieListReport> getMoovieListReports() {

        String sql = "SELECT c FROM MoovieListReport c";

        TypedQuery<MoovieListReport> query = em.createQuery(sql, MoovieListReport.class);

        return query.getResultList();
    }

    @Override
    public List<MoovieListReport> getMoovieListReports(int pageSize, int pageNumber) {

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        String sql = "SELECT c FROM MoovieListReport c ORDER BY c.report_date DESC";

        TypedQuery<MoovieListReport> query = em.createQuery(sql, MoovieListReport.class)
                .setMaxResults(pageSize)
                .setFirstResult(offset);

        return query.getResultList();
    }

    @Override
    public List<MoovieList> getReportedMoovieLists() {

        String sql = "SELECT r FROM MoovieList r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<MoovieList> query = em.createQuery(sql, MoovieList.class);

        return query.getResultList();
    }

    @Override
    public int getReportedMoovieListsCount() {
        String sql = "SELECT r FROM MoovieList r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<MoovieList> query = em.createQuery(sql, MoovieList.class);

        return query.getResultList().size();
    }

    @Override
    public MoovieListReport reportMoovieList(int moovieListId, int userId, int type, String content) {

        MoovieList moovieList = em.find(MoovieList.class, moovieListId);
        User user = em.find(User.class, userId);

        List<MoovieListReport> existingReports = em
                .createQuery(
                        "SELECT m FROM MoovieListReport m WHERE m.moovieList = :moovieList AND m.reportedBy = :user",
                        MoovieListReport.class)
                .setParameter("moovieList", moovieList)
                .setParameter("user", user)
                .getResultList();

        if (!existingReports.isEmpty()) {
            throw new ConflictException("User already reported this moovielist");
        }

        MoovieListReport report = new MoovieListReport(type, content, user, moovieList);
        em.persist(report);
        return report;
    }

    @Override
    public void resolveMoovieListReport(int moovieListId) {
        String sql = "SELECT r FROM MoovieListReport r WHERE r.moovieList.moovieListId = :moovieListId";
        List<MoovieListReport> toRemove = em.createQuery(sql, MoovieListReport.class)
                .setParameter("moovieListId", moovieListId)
                .getResultList();

        for (MoovieListReport report : toRemove) {
            em.remove(report);
        }
    }

    @Override
    public List<CommentReport> getCommentReports() {
        String sql = "SELECT c FROM CommentReport c";

        TypedQuery<CommentReport> query = em.createQuery(sql, CommentReport.class);

        return query.getResultList();

    }

    @Override
    public List<CommentReport> getCommentReports(int pageSize, int pageNumber) {

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        String sql = "SELECT c FROM CommentReport c ORDER BY c.report_date DESC";

        TypedQuery<CommentReport> query = em.createQuery(sql, CommentReport.class)
                .setMaxResults(pageSize)
                .setFirstResult(offset);

        return query.getResultList();
    }

    @Override
    public List<Comment> getReportedComments() {

        String sql = "SELECT r FROM Comment r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);

        return query.getResultList();
    }

    @Override
    public int getReportedCommentsCount() {
        String sql = "SELECT r FROM Comment r WHERE COALESCE(r.totalReports, 0) > 0 ORDER BY r.totalReports DESC";

        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);

        return query.getResultList().size();
    }

    @Override
    public CommentReport reportComment(int commentId, int userId, int type, String content) {
        Comment comment = em.find(Comment.class, commentId);
        User user = em.find(User.class, userId);

        List<CommentReport> existingReports = em.createQuery(
                "SELECT r FROM CommentReport r WHERE r.comment = :comment AND r.reportedBy = :user",
                CommentReport.class)
                .setParameter("comment", comment)
                .setParameter("user", user)
                .getResultList();

        if (!existingReports.isEmpty()) {
            throw new ConflictException("User already reported this comment");
        }

        CommentReport report = new CommentReport(type, content, user, comment);
        em.persist(report);
        return report;
    }

    @Override
    public void resolveCommentReport(int commentId) {
        String sql = "SELECT r FROM CommentReport r WHERE r.comment.commentId = :commentId";
        List<CommentReport> toRemove = em.createQuery(sql, CommentReport.class)
                .setParameter("commentId", commentId)
                .getResultList();

        for (CommentReport report : toRemove) {
            em.remove(report);
        }
    }

    @Override
    public List<Object> getReports(String contentType, Integer reportType, Integer resourceId, int pageSize,
            int pageNumber) {

        List<String> unionQueries = new ArrayList<>();

        // Build individual queries for each content type with appropriate filtering
        if (contentType == null || contentType.equalsIgnoreCase("review")) {
            String reviewQuery = "SELECT reportid, 'REVIEW' AS src, report_date FROM reportsreviews WHERE 1=1";
            if (reportType != null) {
                reviewQuery += " AND type = " + reportType;
            }
            if (resourceId != null) {
                reviewQuery += " AND reviewId = " + resourceId;
            }
            unionQueries.add("(" + reviewQuery + ")");
        }

        if (contentType == null || contentType.equalsIgnoreCase("moovieListReview")) {
            String mlReviewQuery = "SELECT reportid, 'MLREVIEW' AS src, report_date FROM reportsMoovieListReviews WHERE 1=1";
            if (reportType != null) {
                mlReviewQuery += " AND type = " + reportType;
            }
            if (resourceId != null) {
                mlReviewQuery += " AND moovieListReviewId = " + resourceId;
            }
            unionQueries.add("(" + mlReviewQuery + ")");
        }

        if (contentType == null || contentType.equalsIgnoreCase("moovieList")) {
            String moovieListQuery = "SELECT reportid, 'MOOVIELIST' AS src, report_date FROM reportsmoovielists WHERE 1=1";
            if (reportType != null) {
                moovieListQuery += " AND type = " + reportType;
            }
            if (resourceId != null) {
                moovieListQuery += " AND moovieListId = " + resourceId;
            }
            unionQueries.add("(" + moovieListQuery + ")");
        }

        if (contentType == null || contentType.equalsIgnoreCase("comment")) {
            String commentQuery = "SELECT reportid, 'COMMENT' AS src, report_date FROM reportscomments WHERE 1=1";
            if (reportType != null) {
                commentQuery += " AND type = " + reportType;
            }
            if (resourceId != null) {
                commentQuery += " AND commentId = " + resourceId;
            }
            unionQueries.add("(" + commentQuery + ")");
        }

        if (unionQueries.isEmpty()) {
            return new ArrayList<>();
        }

        String sql = String.join(" UNION ALL ", unionQueries) +
                " ORDER BY report_date DESC LIMIT :limit OFFSET :offset";

        int offset = Math.max(pageNumber - 1, 0) * pageSize;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = em.createNativeQuery(sql)
                .setParameter("limit", pageSize)
                .setParameter("offset", offset)
                .getResultList();

        List<Object> reports = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            Number idNumber = (Number) row[0];
            int id = idNumber.intValue();
            String kind = ((String) row[1]).toUpperCase();

            switch (kind) {
                case "REVIEW":
                    reports.add(em.find(ReviewReport.class, id));
                    break;
                case "MLREVIEW":
                    reports.add(em.find(MoovieListReviewReport.class, id));
                    break;
                case "MOOVIELIST":
                    reports.add(em.find(MoovieListReport.class, id));
                    break;
                case "COMMENT":
                    reports.add(em.find(CommentReport.class, id));
                    break;
                default:
                    break;
            }
        }

        return reports;
    }

    @Override
    public int getReportsCount(String contentType, Integer reportType, Integer resourceId) {
        try {

            List<String> countQueries = new ArrayList<>();

            // Build individual count queries for each content type with appropriate
            // filtering
            if (contentType == null || contentType.equalsIgnoreCase("review")) {
                String reviewCountQuery = "SELECT COUNT(*) FROM reportsreviews WHERE 1=1";
                if (reportType != null) {
                    reviewCountQuery += " AND type = " + reportType;
                }
                if (resourceId != null) {
                    reviewCountQuery += " AND reviewId = " + resourceId;
                }
                countQueries.add("(" + reviewCountQuery + ")");
            }

            if (contentType == null || contentType.equalsIgnoreCase("moovieListReview")) {
                String mlReviewCountQuery = "SELECT COUNT(*) FROM reportsMoovieListReviews WHERE 1=1";
                if (reportType != null) {
                    mlReviewCountQuery += " AND type = " + reportType;
                }
                if (resourceId != null) {
                    mlReviewCountQuery += " AND moovieListReviewId = " + resourceId;
                }
                countQueries.add("(" + mlReviewCountQuery + ")");
            }

            if (contentType == null || contentType.equalsIgnoreCase("moovieList")) {
                String moovieListCountQuery = "SELECT COUNT(*) FROM reportsmoovielists WHERE 1=1";
                if (reportType != null) {
                    moovieListCountQuery += " AND type = " + reportType;
                }
                if (resourceId != null) {
                    moovieListCountQuery += " AND moovieListId = " + resourceId;
                }
                countQueries.add("(" + moovieListCountQuery + ")");
            }

            if (contentType == null || contentType.equalsIgnoreCase("comment")) {
                String commentCountQuery = "SELECT COUNT(*) FROM reportscomments WHERE 1=1";
                if (reportType != null) {
                    commentCountQuery += " AND type = " + reportType;
                }
                if (resourceId != null) {
                    commentCountQuery += " AND commentId = " + resourceId;
                }
                countQueries.add("(" + commentCountQuery + ")");
            }

            if (countQueries.isEmpty()) {
                return 0;
            }

            String sql = "SELECT " + String.join(" + ", countQueries) + " AS total_count";

            BigInteger result = (BigInteger) em.createNativeQuery(sql).getSingleResult();
            return result.intValue();

        } catch (Exception e) {

            LOGGER.error("Error getting reports count", e);
            throw new RuntimeException(e);
        }

    }
}
