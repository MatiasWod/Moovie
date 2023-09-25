package ar.edu.itba.paw.services;


import ar.edu.itba.paw.models.User.User;

import java.util.Optional;

public interface UserService {
    User createUser(String username, String email, String password);

    Optional<User> findUserById(int userId);
    Optional<User> findUserByEmail(String mail);

    Optional<User> findUserByUsername(String username);

    User getOrCreateUserViaMail(String mail);
}
