package ar.edu.itba.paw.services;


import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public static final int DEFAULT_PAGE_SIZE_CONTENT = 25;

    public static final int ROLE_NOT_AUTHENTICATED = -1;
    public static final int ROLE_UNREGISTERED = 0;
    public static final int ROLE_USER = 1;
    public static final int ROLE_MODERATOR = 2;

    //Registration actions
    String createUser(String username, String email, String password);
    User createUserFromUnregistered(String username, String email, String password);
    boolean confirmRegister(Token token);

    //User finders
    User findUserById(int userId);
    User findUserByEmail(String mail);
    User findUserByUsername(String username);

    //Search user (recomeneded only for the searchbar)
    List<Profile> searchUsers(String username,  int size, int pageNumber);

    //Return the parameters needed to show in the profile page
    Profile getProfileByUsername(String username);

    //Auth info of users
    User getInfoOfMyUser();
    //Returns -1 if not authenticated
    int tryToGetCurrentUserId();
    boolean isUsernameMe(String username);

    //Profile picture functions
    void setProfilePicture(MultipartFile image);
    byte[] getProfilePicture(String username);

    //Verification mail methods
    void sendVerificationEmail(String email, String username, String token);
    void resendVerificationEmail(String token);
}
