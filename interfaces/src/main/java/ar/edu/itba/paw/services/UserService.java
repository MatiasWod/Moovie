package ar.edu.itba.paw.services;


import ar.edu.itba.paw.models.User.User;

import java.util.Optional;

public interface UserService {
    User createUser(String email);

    Optional<User> findUserById(int userId);
    Optional<User> findUserByEmail(String mail);

    User getOrCreateUserViaMail(String mail);
}
