package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.UnableToCreateUserException;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    public static final  int ROLE_NOT_AUTHENTICATED = -1;
    public static final int ROLE_UNREGISTERED = 0;
    public static final int ROLE_USER = 1;
    public static final int ROLE_MODERATOR = 2;

    User createUser(String username, String email, String password);
    User createUserFromUnregistered(String username, String email, String password);
    boolean confirmRegister(Token token);
    Optional<User> findUserById(int userId);
    Optional<User> findUserByEmail(String mail);

    Optional<User> findUserByUsername(String username);

    User getInfoOfMyUser();

    boolean isUsernameMe(String username);

    void setProfilePicture(MultipartFile image);
    byte[] getProfilePicture(String username);

    void sendVerificationEmail(String email, String username, String token);
}
