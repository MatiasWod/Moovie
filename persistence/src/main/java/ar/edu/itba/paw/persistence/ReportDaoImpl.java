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
import java.math.BigInteger;
import java.util.List;

@Primary
@Repository
public class ReportDaoImpl implements ReportDao{

    @PersistenceContext
    private EntityManager em;

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
        String sql = "SELECT " +
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
    public int getReportedReviewsCount() {
        String sql = "SELECT r FROM Review r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<Review> query = em.createQuery(sql, Review.class);

        return query.getResultList().size();
    }

    @Override
    public void reportReview(int reviewId, int userId, int type, String content) {
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
    public void resolveReviewReport(int reviewId) {
        String sql = "SELECT r FROM MoovieListReviewReport r WHERE r.moovieListReview.moovieListReviewId = :reviewId";
        List<ReviewReport> toRemove = em.createQuery(sql, ReviewReport.class)
                .setParameter("reviewId", reviewId)
                .getResultList();

        for( ReviewReport report : toRemove ){
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
    public List<MoovieListReview> getReportedMoovieListReviews() {

        String sql = "SELECT r FROM MoovieListReview r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<MoovieListReview> query = em.createQuery(sql, MoovieListReview.class);

        return query.getResultList();
    }

    @Override
    public int getReportedMoovieListReviewsCount() {
        String sql = "SELECT r FROM MoovieListReview r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<MoovieListReview> query = em.createQuery(sql, MoovieListReview.class);

        return query.getResultList().size();
    }

    @Override
    public void reportMoovieListReview(int moovieListReviewId, int userId, int type, String content) {
        String sql = "INSERT INTO reportsmoovielistreviews " +
                "(type, content, reportedBy, moovieListReviewId) " +
                "VALUES ( :type, :content, :userId , :moovieListReviewId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("moovieListReviewId", moovieListReviewId)
                .executeUpdate();
    }

    @Override
    public void resolveMoovieListReviewReport(int moovieListReviewId) {
        String sql = "SELECT r FROM MoovieListReviewReport r WHERE r.moovieListReview.moovieListReviewId = :moovieListReviewId";
        List<MoovieListReviewReport> toRemove = em.createQuery(sql, MoovieListReviewReport.class)
                .setParameter("moovieListReviewId", moovieListReviewId)
                .getResultList();

        for( MoovieListReviewReport report : toRemove ){
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
    public List<MoovieList> getReportedMoovieLists() {

        String sql = "SELECT r FROM MoovieList r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<MoovieList> query = em.createQuery(sql, MoovieList.class);

        return query.getResultList();
    }

    @Override
    public int getReportedMoovieListsCount() {
        String sql = "SELECT r FROM MoovieList r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<MoovieList> query = em.createQuery(sql, MoovieList.class);

        return query.getResultList().size();
    }

    @Override
    public void reportMoovieList(int moovieListId, int userId, int type, String content) {
        String sql = "INSERT INTO reportsmoovielists " +
                "(type, content, reportedBy, moovieListId) " +
                "VALUES ( :type, :content, :userId , :moovieListId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .executeUpdate();
    }

    @Override
    public void resolveMoovieListReport(int moovieListId) {
        String sql = "SELECT r FROM MoovieListReport r WHERE r.moovieList.moovieListId = :moovieListId";
        List<MoovieListReport> toRemove = em.createQuery(sql, MoovieListReport.class)
                .setParameter("moovieListId", moovieListId)
                .getResultList();

        for( MoovieListReport report : toRemove ){
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
    public List<Comment> getReportedComments() {

        String sql = "SELECT r FROM Comment r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);

        return query.getResultList();
    }

    @Override
    public int getReportedCommentsCount() {
        String sql = "SELECT r FROM Comment r WHERE COALESCE(r.totalReports, 0) > 0";

        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);

        return query.getResultList().size();
    }

    @Override
    public void reportComment(int commentId, int userId, int type, String content) {
        String sql = "INSERT INTO reportscomments " +
                "(type, content, reportedBy, commentId) " +
                "VALUES ( :type, :content, :userId , :commentId )";

        em.createNativeQuery(sql)
                .setParameter("type", type)
                .setParameter("content", content)
                .setParameter("userId", userId)
                .setParameter("commentId", commentId)
                .executeUpdate();
    }

    @Override
    public void resolveCommentReport(int commentId) {
        String sql = "SELECT r FROM CommentReport r WHERE r.comment.commentId = :commentId";
        List<CommentReport> toRemove = em.createQuery(sql, CommentReport.class)
                .setParameter("commentId", commentId)
                .getResultList();

        for( CommentReport report : toRemove ){
            em.remove(report);
        }
    }
}
