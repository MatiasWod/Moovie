package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenService verificationTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    //REGSITRATION


    @Transactional
    @Override
    public String createUser(String username, String email, String password){
        if(userDao.findUserByUsername(username).isPresent()){
            LOGGER.info("Failed to created user with username: {}", username);
            throw new UnableToCreateUserException("username_taken");
        }
        Optional<User> aux = userDao.findUserByEmail(email);

        if(aux.isPresent()){
            if(aux.get().getRole() == UserRoles.UNREGISTERED.getRole()){
                User user = createUserFromUnregistered(username, email, password);
                String token = verificationTokenService.createVerificationToken(user.getUserId());
                emailService.sendVerificationEmail(user,token,LocaleContextHolder.getLocale());
                LOGGER.info("Succesfuly created user with username: {}", username);
                return token;
            } else{
                throw new UnableToCreateUserException("email_taken");
            }
        }
        try{
            User user = userDao.createUser(username, email, passwordEncoder.encode(password));
            LOGGER.info("Succesfuly created user with username: {}", username);
            String token = verificationTokenService.createVerificationToken(user.getUserId());
            emailService.sendVerificationEmail(user,token,LocaleContextHolder.getLocale());
            return token;
        } catch (Exception e){
            throw new UnableToCreateUserException("Unable to create user");
        }
    }

    @Transactional
    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        try{
            return userDao.createUserFromUnregistered(username, email, passwordEncoder.encode(password));
        } catch (Exception e){
            throw new UnableToCreateUserException("Unable to create user");
        }

    }

    @Transactional
    @Override
    public boolean confirmRegister(Token token) {
        boolean isValidToken = verificationTokenService.isValidToken(token);
        if(isValidToken) {
            int userId = token.getUserId();
            int role = findUserById(userId).getRole();

            if(role == UserRoles.UNREGISTERED.getRole() || role == UserRoles.NOT_AUTHENTICATED.getRole()){
                userDao.confirmRegister(token.getUserId(), UserRoles.USER.getRole());
            } else if(role == UserRoles.MODERATOR_NOT_REGISTERED.getRole()){
                userDao.confirmRegister(token.getUserId(), UserRoles.MODERATOR.getRole());
            } else if(role == UserRoles.BANNED_NOT_REGISTERED.getRole()){
                userDao.confirmRegister(token.getUserId(), UserRoles.BANNED.getRole());
            }

            verificationTokenService.deleteToken(token);
        }
        moovieListDao.createMoovieList(token.getUserId(), "Watched" , MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType() , "" );
        moovieListDao.createMoovieList(token.getUserId(), "Watchlist", MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),  "" );

        LOGGER.info("Succesfuly confirmed user with userid: {}", token.getUserId());
        return isValidToken;
    }



    //FIND USERS


    @Transactional(readOnly = true)
    @Override
    public User findUserById(int userId) {
        return userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("User with id: " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email).orElseThrow(() -> new UnableToFindUserException("User with email: " + email + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username).orElseThrow(() -> new UnableToFindUserException("User with username: " + username + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Profile> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber) {
        return userDao.searchUsers(username, setOrderBy(orderBy) , setSortOrder(sortOrder) , size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public int getSearchCount(String username) {
        return userDao.getSearchCount(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int getLikedMoovieListCountForUser(String username) {
        return userDao.getLikedMoovieListCountForUser(username);
    }

    @Transactional(readOnly = true)
    @Override
    public int getUserCount() {
        return userDao.getUserCount();
    }

    @Transactional(readOnly = true)
    @Override
    public Profile getProfileByUsername(String username) {
        return userDao.getProfileByUsername(username).orElseThrow(() -> new UnableToFindUserException("No user with username: " + username));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Profile> getMilkyPointsLeaders(int size, int pageNumber) {
        return userDao.getMilkyPointsLeaders(size,pageNumber);
    }

    //AUTHENTICATION INFO

    @Override
    public User getInfoOfMyUser() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return findUserByUsername(userDetails.getUsername());
        } else {
            throw new UserNotLoggedException("User is not logged when its supposed");
        }
    }

    @Override
    public int tryToGetCurrentUserId() {
        try{
            return getInfoOfMyUser().getUserId();
        } catch(UserNotLoggedException e){
            return -1;
        }
    }

    @Override
    public boolean isUsernameMe(String username) {
        if( getInfoOfMyUser().getUsername().equals(username)  ){
            return true;
        }
        return false;
    }


    //PROFILE PICTURES


    @Transactional
    @Override
    public void setProfilePicture(MultipartFile picture) {
        int uid = getInfoOfMyUser().getUserId();

        if(!picture.isEmpty()){
            if (!(picture.getContentType() != null && picture.getContentType().startsWith("image/"))) {
                throw new InvalidTypeException("File is not of type image");
            }
            try {
                byte[] image = IOUtils.toByteArray(picture.getInputStream());
                if(userDao.hasProfilePicture(uid)){
                    userDao.updateProfilePicture( getInfoOfMyUser().getUserId() , image);
                    LOGGER.info("Succesfully set the profile picture for user with userid : {} ", uid);
                    return;
                }
                LOGGER.info("Succesfully set the profile picture for user with userid : {} ", uid);
                userDao.setProfilePicture( getInfoOfMyUser().getUserId() , image);
            }
            catch (IOException e){
                throw new FailedToSetProfilePictureException("The upload of the profile picture failed");
            }
        }else{
            throw new NoFileException("No file was selected");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getProfilePicture(String username) {
        return userDao.getProfilePicture(findUserByUsername(username).getUserId()).orElseThrow(() -> new NoFileException("Profile picture not found")).getImage();
    }

    @Async
    @Transactional(readOnly = true)
    @Override
    public void resendVerificationEmail(Token token) {
        verificationTokenService.renewToken(token);
        User toRenewTokenUser = userDao.findUserById(token.getUserId()).orElseThrow(UnableToFindUserException::new);
        emailService.sendVerificationEmail(toRenewTokenUser, token.getToken(), LocaleContextHolder.getLocale());
    }

    private String setSortOrder(String sortOrder){
        if(sortOrder==null || sortOrder.isEmpty()){
            return null;
        }
        sortOrder = sortOrder.replaceAll(" ","");
        if(sortOrder.toLowerCase().equals("asc")){
            return "asc";
        }
        if(sortOrder.toLowerCase().equals("desc")){
            return "desc";
        }
        return null;
    }

    private String setOrderBy(String orderBy){
        if(orderBy==null || orderBy.isEmpty()){
            return null;
        }
        orderBy = orderBy.replaceAll(" ","");
        if(orderBy.toLowerCase().equals("milkypoints")){
            return "milkyPoints";
        }
        if(orderBy.toLowerCase().equals("userid")){
            return "userId";
        }
        if(orderBy.toLowerCase().equals("username")){
            return "username";
        }
        return null;
    }
}

