package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.User;

import java.util.Optional;

public interface UserDao {

    public static final int ROLE_UNREGISTERED = 0;
    public static final int ROLE_USER = 1;
    public static final int ROLE_MODERATOR = 2;

    User createUser(String username, String email, String password);
    User createUserFromUnregistered(String username, String email, String password);

    Optional<User> findUserById(int userId);
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
