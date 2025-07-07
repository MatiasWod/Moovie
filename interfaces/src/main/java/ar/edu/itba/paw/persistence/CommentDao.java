package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedback;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;
import ar.edu.itba.paw.models.User.User;

import java.util.List;

public interface CommentDao {

    List<Comment> getComments(int reviewId, int userId, int size, int pageNumber);
    List<Comment> getCommentsForUsername(int userId, int size, int pageNumber);

    Comment getCommentById(int commentId);

    boolean userHasLiked(int commentId, int userId);
    boolean userHasDisliked(int commentId, int userId);

    void likeComment(int commentId, int userId);
    void removeLikeComment(int commentId, int userId);

    void dislikeComment(int commentId, int userId);
    void removeDislikeComment(int commentId, int userId);

    void createComment(int reviewId, String content, User user);

    void deleteComment(int commentId);

    List<Comment> getCommentFeedbackForUser(int uid, int pageNumber, int pageSize);
    int getCommentFeedbackForUserCount(int uid);
    List<CommentFeedback> getCommentFeedbackForComment(int commentId, int pageNumber, int pageSize);
    List<CommentFeedback> getCommentFeedbackForComment(int commentId, CommentFeedbackType feedback, int pageNumber, int pageSize);
    int getCommentFeedbackForCommentCount(int commentId);
    int getCommentFeedbackForCommentCount(int commentId, CommentFeedbackType feedback);
}
