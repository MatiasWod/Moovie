package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedback;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;
import ar.edu.itba.paw.models.User.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> getComments(int reviewId, int userId, int size, int pageSize) {
        String sql = "SELECT c" +
                " FROM Comment c WHERE c.reviewId = :reviewId";

        List<Comment> comments = em.createQuery(sql, Comment.class)
                .setParameter("reviewId", reviewId)
                .setFirstResult(pageSize*size)
                .setMaxResults(size)
                .getResultList();

        for (Comment c : comments){
            c.setCurrentUserHasLiked(userHasLiked(c.getCommentId(),userId));
            c.setCurrentUserHasDisliked(userHasDisliked(c.getCommentId(),userId));
        }

        return comments;
    }

    @Override
    public Comment getCommentById(int commentId) {
        return em.find(Comment.class, commentId);
    }

    @Override
    public boolean userHasLiked(int commentId, int userId) {
        String sqlQuery = "SELECT CASE WHEN " +
                "EXISTS( SELECT 1 FROM commentlikes cl WHERE cl.commentid = :commentId AND cl.userid = :userId ) " +
                "THEN true ELSE false END";

        boolean likeState = (boolean) em.createNativeQuery(sqlQuery)
                .setParameter("commentId", commentId)
                .setParameter("userId", userId)
                .getSingleResult();
        return likeState;
    }

    @Override
    public boolean userHasDisliked(int commentId, int userId) {
        String sqlQuery = "SELECT CASE WHEN " +
                "EXISTS( SELECT 1 FROM commentdislikes cl WHERE cl.commentid = :commentId AND cl.userid = :userId ) " +
                "THEN true ELSE false END";

        boolean dislikeState = (boolean) em.createNativeQuery(sqlQuery)
                .setParameter("commentId", commentId)
                .setParameter("userId", userId)
                .getSingleResult();
        return dislikeState;
    }

    @Override
    public void likeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("INSERT INTO commentlikes (commentid, userid) VALUES (:commentId, :userId)");
        query.setParameter("commentId", commentId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void removeLikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("DELETE FROM commentlikes WHERE userid = :userId AND commentId = :commentId");
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        query.executeUpdate();
    }

    @Override
    public void dislikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("INSERT INTO commentdislikes (commentid, userid) VALUES (:commentId, :userId)");
        query.setParameter("commentId", commentId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void removeDislikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("DELETE FROM commentdislikes WHERE userid = :userId AND commentId = :commentId");
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        query.executeUpdate();
    }

    @Override
    public void createComment(int reviewId, String content, User user) {
        em.persist(new Comment(user,reviewId,content));
    }

    @Override
    public void deleteComment(int commentId) {
        Comment toDelete = em.find(Comment.class, commentId);
        if ( toDelete != null ){
            em.remove(toDelete);
        }
    }
/*
/final TypedQuery<User> query = em.createQuery(
                "SELECT cl.user FROM CommentLikes cl WHERE cl.comment.commentId = :commentId",
                User.class
        );
        query.setParameter("commentId", commentId);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
 */

    @Override
    public List<Comment> getCommentFeedbackForUser(int uid, int pageNumber, int pageSize) {
        int likeCount = getLikedCommentForUserCount(uid);
        int dislikeCount = getDislikedCommentForUserCount(uid);

        int offset = pageNumber * pageSize;

        List<Comment> result = new ArrayList<>();

        if (offset < likeCount) {
            int likeOffset = offset;
            int likeLimit = Math.min(pageSize, likeCount - likeOffset);
            if(likeLimit>0){
                result.addAll(getLikedComments(uid, likeLimit, likeOffset));
            }
            else{
                likeLimit = 0;
            }

            int remaining = pageSize - likeLimit;
            if (remaining > 0 && dislikeCount > 0) {
                result.addAll(getDislikedComments(uid, remaining, 0));
            }
        } else {
            int dislikeOffset = offset - likeCount;
            int dislikeLimit = Math.min(pageSize, dislikeCount - dislikeOffset);
            if(dislikeLimit > 0){
                result.addAll(getDislikedComments(uid, dislikeLimit, dislikeOffset));
            }
        }

        return result;
    }


    public List<Comment> getDislikedComments(int uid, int limit, int offset) {
        final String jpql = "SELECT c FROM CommentDislike cd " +
                "JOIN Comment c ON cd.commentId = c.commentId " +
                "WHERE cd.userId = :uid " +
                "ORDER BY cd.commentId";

        return em.createQuery(jpql, Comment.class)
                .setParameter("uid", uid)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }



    public List<Comment> getLikedComments(int uid, int limit, int offset) {
        final String jpql = "SELECT c FROM CommentLike cl " +
                "JOIN Comment c ON cl.commentId = c.commentId " +
                "WHERE cl.userId = :uid " +
                "ORDER BY cl.commentId";

        return em.createQuery(jpql, Comment.class)
                .setParameter("uid", uid)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }



    public int getLikedCommentForUserCount(int uid) {
        final TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(cl) FROM CommentLike  cl WHERE cl.userId = :uid",
                Long.class
        );
        query.setParameter("uid", uid);
        return query.getSingleResult().intValue();
    }

    public int getDislikedCommentForUserCount(int uid) {
        final TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(cl) FROM CommentDislike cl WHERE cl.userId = :uid",
                Long.class
        );
        query.setParameter("uid", uid);
        return query.getSingleResult().intValue();
    }


    @Override
    public int getCommentFeedbackForUserCount(int uid) {
        return getLikedCommentForUserCount(uid) + getDislikedCommentForUserCount(uid);
    }

    @Override
    public List<CommentFeedback> getCommentFeedbackForComment(int commentId, int pageNumber, int pageSize) {
        int likeCount = getLikedFeedbackForCommentCount(commentId);
        int dislikeCount = getDislikedFeedbackForCommentCount(commentId);

        int offset = pageNumber * pageSize;

        List<CommentFeedback> result = new ArrayList<>();

        if (offset < likeCount) {
            int likeOffset = offset;
            int likeLimit = Math.min(pageSize, likeCount - likeOffset);
            if(likeLimit>0){
                result.addAll(getLikedFeedbackForComment(commentId, likeLimit, likeOffset));
            }
            else{
                likeLimit = 0;
            }

            int remaining = pageSize - likeLimit;
            if (remaining > 0 && dislikeCount > 0) {
                result.addAll(getDislikedFeedbackForComment(commentId, remaining, 0));
            }
        } else {
            int dislikeOffset = offset - likeCount;
            int dislikeLimit = Math.min(pageSize, dislikeCount - dislikeOffset);
            if(dislikeLimit>0){
                result.addAll(getDislikedFeedbackForComment(commentId, dislikeLimit, dislikeOffset));
            }
        }

        return result;
    }


    @Override
    public int getCommentFeedbackForCommentCount(int commentId) {
        return getLikedFeedbackForCommentCount(commentId) + getDislikedFeedbackForCommentCount(commentId);
    }


    public List<CommentFeedback> getLikedFeedbackForComment(int commentId, int limit, int offset) {
        final String sql = "SELECT u.username FROM commentlikes cl " +
                "JOIN users u ON u.userid = cl.userid " +
                "WHERE cl.commentid = :commentId ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("commentId", commentId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        List<String> usernames = query.getResultList();

        List<CommentFeedback> feedback = new ArrayList<>();
        for (String username : usernames) {
            feedback.add(new CommentFeedback(CommentFeedbackType.LIKE, username, commentId));
        }

        return feedback;
    }


    public int getLikedFeedbackForCommentCount(int commentId) {
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM commentlikes WHERE commentid = :commentId");
        query.setParameter("commentId", commentId);
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }

    public List<CommentFeedback> getDislikedFeedbackForComment(int commentId, int limit, int offset) {
        final String sql = "SELECT u.username FROM commentdislikes cd " +
                "JOIN users u ON u.userid = cd.userid " +
                "WHERE cd.commentid = :commentId ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("commentId", commentId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        List<String> usernames = query.getResultList();

        List<CommentFeedback> feedback = new ArrayList<>();
        for (String username : usernames) {
            feedback.add(new CommentFeedback(CommentFeedbackType.DISLIKE, username, commentId));
        }

        return feedback;
    }


    public int getDislikedFeedbackForCommentCount(int commentId) {
        final Query query = em.createNativeQuery("SELECT COUNT(*) FROM commentdislikes WHERE commentid = :commentId");
        query.setParameter("commentId", commentId);
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }



}
