package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedback;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;

import java.util.List;

public interface CommentService {

    List<Comment> getComments(int reviewId, int size, int pageNumber);
    List<Comment> getCommentsForUsername(int userId, int size, int pageNumber);

    Comment getCommentById(int commentId);

    boolean userHasLiked(int commentId, int userId);
    boolean userHasDisliked(int commentId, int userId);

    boolean likeComment(int commentId);

    void removeLikeComment(int commentId);

    boolean dislikeComment(int commentId);

    void removeDislikeComment(int commentId);

    void createComment(int reviewId, String content);

    void deleteComment(int commentId);

    List<Comment> getCommentFeedbackForUser(String username, int pageNumber, int pageSize);
    int getCommentFeedbackForUserCount(String username);
    List<CommentFeedback> getCommentFeedbackForComment(int commentId, int pageNumber, int pageSize);
    List<CommentFeedback> getCommentFeedbackForComment(int commentId, CommentFeedbackType feedback, int pageNumber, int pageSize);
    int getCommentFeedbackForCommentCount(int commentId);
    int getCommentFeedbackForCommentCount(int commentId, CommentFeedbackType feedback);

}
