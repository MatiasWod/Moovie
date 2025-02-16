package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("accessValidator")
public class AccessValidator {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MoovieListService listService;

    public boolean checkIsUser (String username) {
        return userService.findUserByUsername(username) != null;
    }

    public boolean isUserLoggedIn() {
        return userService.getInfoOfMyUser() != null;
    }

    public boolean isUserCommentAuthor(int commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            User currentUser = userService.getInfoOfMyUser();

            if (comment == null || currentUser == null) {
                return false;
            }

            return currentUser.getUserId() == comment.getUserId();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserListAuthor(int listId) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            MoovieList list = listService.getMoovieListById(listId);
            return currentUser != null && currentUser.getUserId() == list.getUserId();
        } catch (Exception e) {
            return false;
        }
    }
}
