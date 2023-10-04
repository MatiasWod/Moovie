package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import ar.edu.itba.paw.models.User.Token;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final MoovieListDao moovieListDao;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final VerificationTokenService verificationTokenService;
    private static final int ROLE_NOT_AUTHENTICATED = -1;
    private static final int ROLE_UNREGISTERED = 0;
    private static final int ROLE_USER = 1;
    private static final int ROLE_MODERATOR = 2;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, EmailService emailService,
                           MessageSource messageSource, VerificationTokenService verificationTokenService, MoovieListDao moovieListDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.messageSource = messageSource;
        this.verificationTokenService = verificationTokenService;
        this.moovieListDao = moovieListDao;
    }



    //REGSITRATION


    @Override
    public void createUser(String username, String email, String password){
        if(userDao.findUserByUsername(username).isPresent()){
            throw new UnableToCreateUserException("username_taken");
        }
        Optional<User> aux = userDao.findUserByEmail(email);

        if(aux.isPresent()){
            if(aux.get().getRole() == ROLE_UNREGISTERED){
                User user = createUserFromUnregistered(username, email, password);
                String token = verificationTokenService.createVerificationToken(user.getUserId());
                sendVerificationEmail(email,username,token);
                return;
            } else{
                throw new UnableToCreateUserException("email_taken");
            }
        }
        User user = userDao.createUser(username, email, passwordEncoder.encode(password));
        String token = verificationTokenService.createVerificationToken(user.getUserId());
        sendVerificationEmail(email,username,token);
    }

    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        return userDao.createUserFromUnregistered(username, email, passwordEncoder.encode(password));
    }

    @Override
    public boolean confirmRegister(Token token) {
        boolean isValidToken = verificationTokenService.isValidToken(token);
        if(isValidToken) {
            userDao.confirmRegister(token.getUserId(), ROLE_USER);
            verificationTokenService.deleteToken(token);
        }
        moovieListDao.createMoovieList(token.getUserId(), "Watched" , moovieListDao.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE, "" );
        moovieListDao.createMoovieList(token.getUserId(), "Watchlist", moovieListDao.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE,  "" );
        return isValidToken;
    }



    //FIND USERS


    @Override
    public User findUserById(int userId) {
        return userDao.findUserById(userId).orElseThrow(() -> new UnableToFindUserException("User with id: " + userId + " not found"));
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email).orElseThrow(() -> new UnableToFindUserException("User with email: " + email + " not found"));
    }

    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username).orElseThrow(() -> new UnableToFindUserException("User with username: " + username + " not found"));
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
    public boolean isUsernameMe(String username) {
        if( getInfoOfMyUser().getUsername().equals(username)  ){
            return true;
        }
        return false;
    }


    //PROFILE PICTURES


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
                    return;
                }
                userDao.setProfilePicture( getInfoOfMyUser().getUserId() , image);
            }
            catch (IOException e){
                throw new FailedToSetProfilePictureException("The upload of the profile picture failed");
            }
        }else{
            throw new NoFileException("No file was selected");
        }
    }

    @Override
    public byte[] getProfilePicture(String username) {
        return userDao.getProfilePicture(findUserByUsername(username).getUserId()).get().getImage();
    }


    //EMAIL VERIFICATION


    public void sendVerificationEmail(String email, String username, String token) {
        final Map<String,Object> mailMap = new HashMap<>();
        mailMap.put("username",username);
        mailMap.put("token",token);
        final String subject = messageSource.getMessage("email.confirmation.subject",null, Locale.getDefault());
        emailService.sendEmail(email,subject,"confirmationMail.html",mailMap);
    }

    @Override
    public void resendVerificationEmail(String token) {
        verificationTokenService.renewToken(token);
        verificationTokenService.getToken(token).ifPresent(token1 -> {
            userDao.findUserById(token1.getUserId()).ifPresent(user -> {
                sendVerificationEmail(user.getEmail(), user.getUsername(), token1.getToken());
                    }
            );
        });
    }
}

