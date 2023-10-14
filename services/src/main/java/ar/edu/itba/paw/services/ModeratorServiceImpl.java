package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAuthenticationLevelRequiredToPerformActionException;
import ar.edu.itba.paw.exceptions.UnableToBanUserException;
import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.BannedDao;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.ReviewDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModeratorServiceImpl implements ModeratorService{
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private MoovieListDao moovieListDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BannedDao bannedDao;


    @Override
    public void deleteReview(int reviewId, int mediaId) {
        amIModerator();
        reviewDao.deleteReview(reviewId);
        mediaService.downMediaVoteCount(mediaId);
    }

    @Override
    public void deleteMoovieListList(int moovieListId) {
        amIModerator();
        moovieListDao.deleteMoovieList(moovieListId);
    }

    @Override
    public void banUser(int userId, String message) {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId ));
        if(u.getRole() == userDao.ROLE_MODERATOR){
            throw new UnableToBanUserException("Cannot ban another moderator");
        }
        if(u.getRole() == userDao.ROLE_UNREGISTERED){
            throw new UnableToBanUserException("Cannot ban an unregisted user");
        }
        userDao.changeUserRole(userId, userDao.ROLE_BANNED);
        bannedDao.createBannedMessage(userId, userService.getInfoOfMyUser().getUserId(), message);

        final Map<String,Object> mailMap = new HashMap<>();
        mailMap.put("username", u.getUsername());
        mailMap.put("modUsername", userService.getInfoOfMyUser().getUsername());
        mailMap.put("message", message);
        emailService.sendEmail(u.getEmail(),"You have been baned from Moovie", "youHaveBeenBannedMail.html", mailMap);
    }

    @Override
    public void unbanUser(int userId) {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId ));
        if(u.getRole() == userDao.ROLE_MODERATOR){
            throw new UnableToBanUserException("Cannot unban another moderator");
        }
        if(u.getRole() == userDao.ROLE_UNREGISTERED){
            throw new UnableToBanUserException("Cannot ban an unregisted user");
        }
        userDao.changeUserRole(userId, userDao.ROLE_USER);
        bannedDao.deleteBannedMessage(userId);

        final Map<String,Object> mailMap = new HashMap<>();
        mailMap.put("username", u.getUsername());
        emailService.sendEmail(u.getEmail(),"You have been unbaned from Moovie", "youHaveBeenUnbannedMail.html", mailMap);
    }



    @Override
    public void makeUserModerator(int userId) {
        amIModerator();
        User u = userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("No user for the id = " + userId ));
        if(u.getRole() != userDao.ROLE_USER ){
            throw new UnableToChangeRoleException("Unable to change role of uid: " + userId + ", user must be ROLE_USER");
        }
        userDao.changeUserRole(userId, userDao.ROLE_MODERATOR);
    }

    private void amIModerator(){
        if(userService.getInfoOfMyUser().getRole() != userService.ROLE_MODERATOR){
            throw new InvalidAuthenticationLevelRequiredToPerformActionException("To perform this action you must have role: moderator");
        }
    }

}
