package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;

import java.util.List;

public interface UserService {

    //API
    List<User> listAll(int size, int pageNumber);

    List<User> listAll(int role,int size, int pageNumber);

    //Registration actions
    String createUser(String username, String email, String password);

    User createUserFromUnregistered(String username, String email, String password);

    boolean confirmRegister(Token token);

    String forgotPassword(User user);

    boolean resetPassword(Token token, String newPassword);

    //User finders
    User findUserById(int userId);

    User findUserByEmail(String mail);

    User findUserByUsername(String username) throws UnableToFindUserException;

    //Search user (recomeneded only for the searchbar)
    List<User> searchUsers(String username, String orderBy, String sortOrder, int size, int pageNumber);

    //Search user count for pagination in searchUsers cases
    int getSearchCount(String username);

    //Liked amount lof list for a user
    int getLikedMoovieListCountForUser(String username);

    //returns total user count
    int getUserCount();

    int getUserCount(UserRoles role);

    //Returns a list of the users with most milkyPoints
    List<User> getMilkyPointsLeaders(int size, int pageNumber);

    //Auth info of users
    User getInfoOfMyUser();

    //Returns -1 if not authenticated
    int tryToGetCurrentUserId();

    boolean isUsernameMe(String username);

    //Verification mail methods
    void resendVerificationEmail(Token token);
}
