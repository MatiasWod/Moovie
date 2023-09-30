package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.ReviewDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ModeratorServiceImpl implements ModeratorService{
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private MoovieListDao moovieListDao;
    @Autowired
    private UserDao userDao;


    @Override
    public void deleteReview(int reviewId) {
        reviewDao.deleteReview(reviewId);
    }

    @Override
    public void deleteMoovieListList(int moovieListId) {
        moovieListDao.deleteMoovieList(moovieListId);
    }

    @Override
    public void banUser(int userId) {
        userDao.changeUserRole(userId, userDao.ROLE_BANNED);
    }

    @Override
    public void makeUserModerator(int userId) {
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId ));
        if(u.getRole() != userDao.ROLE_USER ){
            throw new UnableToChangeRoleException("Unable to change role of uid: " + userId + ", user must be ROLE_USER");
        }
        userDao.changeUserRole(userId, userDao.ROLE_MODERATOR);
    }
}
