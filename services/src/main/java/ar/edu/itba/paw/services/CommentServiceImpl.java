package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.CommentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private UserService userService;

    @Autowired
    private CommentDao commentDao;


    @Transactional(readOnly = true)
    @Override
    public List<Comment> getComments(int reviewId, int size, int pageNumber) {
        return commentDao.getComments(reviewId, userService.tryToGetCurrentUserId(), size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment getCommentById(int commentId) {
        return commentDao.getCommentById(commentId);
    }

    @Override
    public boolean userHasLiked(int commentId, int userId) {
        return commentDao.userHasLiked(commentId, userId);
    }

    @Override
    public boolean userHasDisliked(int commentId, int userId) {
        return commentDao.userHasDisliked(commentId,userId);
    }

    @Transactional
    @Override
    public void likeComment(int commentId) {
        User currentUser = userService.getInfoOfMyUser();
        boolean hasLiked = commentDao.userHasLiked(commentId, currentUser.getUserId());
        boolean hasDisliked = commentDao.userHasDisliked(commentId, currentUser.getUserId());
        if (!hasDisliked){
            if (!hasLiked){
                commentDao.likeComment(commentId, currentUser.getUserId());
            }else{
                commentDao.removeLikeComment(commentId,currentUser.getUserId());
            }
        }else{
            removeDislikeComment(commentId);
            likeComment(commentId);
        }

    }

    @Transactional
    @Override
    public void removeLikeComment(int commentId) {
        commentDao.removeLikeComment(commentId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void dislikeComment(int commentId) {
        User currentUser = userService.getInfoOfMyUser();
        boolean hasLiked = commentDao.userHasLiked(commentId, currentUser.getUserId());
        boolean hasDisliked = commentDao.userHasDisliked(commentId, currentUser.getUserId());
        if (!hasLiked){
            if (!hasDisliked){
                commentDao.dislikeComment(commentId, currentUser.getUserId());
            }else{
                commentDao.removeDislikeComment(commentId,currentUser.getUserId());
            }
        }else{
            removeLikeComment(commentId);
            dislikeComment(commentId);
        }
    }

    @Transactional
    @Override
    public void removeDislikeComment(int commentId) {
        commentDao.removeDislikeComment(commentId, userService.getInfoOfMyUser().getUserId());
    }

    @Transactional
    @Override
    public void createComment(int reviewId, String content) {
        commentDao.createComment(reviewId, content, userService.getInfoOfMyUser());
    }

    @Transactional
    @Override
    public void deleteComment(int commentId) {
        commentDao.deleteComment(commentId);
    }
}
