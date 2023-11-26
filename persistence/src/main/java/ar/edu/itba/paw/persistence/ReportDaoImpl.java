package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Reports.CommentReport;
import ar.edu.itba.paw.models.Reports.MoovieListReport;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Primary
@Repository
public class ReportDaoImpl implements ReportDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ReviewReport> getReviewReports() {

        String sql = "SELECT c FROM ReviewReport c";

        TypedQuery<ReviewReport> query = em.createQuery(sql, ReviewReport.class);

        return query.getResultList();
    }

    @Override
    public List<Review> getReportedReviews() {
        String sql = "SELECT r FROM Review r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<Review> query = em.createQuery(sql, Review.class);

        return query.getResultList();
    }

    @Override
    public void reportReview(int reviewId, int userId, String type, String content) {
        String sql = "INSERT INTO reportsreviews " +
                "(type, content, reportedBy, reviewId) " +
                "VALUES ( :type, :content, :userId , :reviewId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("reviewId", reviewId)
                .executeUpdate();
    }

    @Override
    public void resolveReviewReport(int reportId) {
        ReviewReport cr = em.find(ReviewReport.class, reportId);
        if (cr != null)
            cr.setResolved(true);
    }

    @Override
    public List<MoovieListReviewReport> getMoovieListReviewReports() {

        String sql = "SELECT c FROM MoovieListReviewReport c";

        TypedQuery<MoovieListReviewReport> query = em.createQuery(sql, MoovieListReviewReport.class);

        return query.getResultList();
    }

    @Override
    public List<MoovieListReview> getReportedMoovieListReviews() {
        return null;
    }

    @Override
    public void reportMoovieListReview(int moovieListReviewId, int userId, String type, String content) {
        String sql = "INSERT INTO reportsmoovielistreviews " +
                "(type, content, reportedBy, reviewId) " +
                "VALUES ( :type, :content, :userId , :moovieListReviewId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("moovieListReviewId", moovieListReviewId)
                .executeUpdate();
    }

    @Override
    public void resolveMoovieListReviewReport(int reportId) {
        MoovieListReviewReport cr = em.find(MoovieListReviewReport.class, reportId);
        if (cr != null)
            cr.setResolved(true);
    }

    @Override
    public List<MoovieListReport> getMoovieListReports() {

        String sql = "SELECT c FROM MoovieListReport c";

        TypedQuery<MoovieListReport> query = em.createQuery(sql, MoovieListReport.class);

        return query.getResultList();
    }

    @Override
    public List<MoovieList> getReportedMoovieLists() {
        return null;
    }

    @Override
    public void reportMoovieList(int moovieListId, int userId, String type, String content) {
        String sql = "INSERT INTO reportsmoovielists " +
                "(type, content, reportedBy, reviewId) " +
                "VALUES ( :type, :content, :userId , :moovieListId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .executeUpdate();
    }

    @Override
    public void resolveMoovieListReport(int reportId) {
        MoovieListReport cr = em.find(MoovieListReport.class, reportId);
        if (cr != null)
            cr.setResolved(true);
    }

    @Override
    public List<CommentReport> getCommentReports() {
        String sql = "SELECT c FROM CommentReport c";

        TypedQuery<CommentReport> query = em.createQuery(sql, CommentReport.class);

        return query.getResultList();

    }

    @Override
    public List<Comment> getReportedComments() {
        return null;
    }

    @Override
    public void reportComment(int commentId, int userId, String type, String content) {
        String sql = "INSERT INTO reportscomments " +
                "(type, content, reportedBy, reviewId) " +
                "VALUES ( :type, :content, :userId , :commentId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("commentId", commentId)
                .executeUpdate();
    }

    @Override
    public void resolveCommentReport(int reportId) {
        CommentReport cr = em.find(CommentReport.class, reportId);
        if (cr != null)
            cr.setResolved(true);
    }
}
