package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.User;

import java.util.Optional;

public interface UserDao {
    User createUser(String email);

    Optional<User> findUserById(int userId);
    Optional<User> findUserByEmail(String email);
}
