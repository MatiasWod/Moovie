package ar.edu.itba.paw.services;


import ar.edu.itba.paw.models.User.User;

public interface UserService {
    User createUser(String email);

    User findUserById(int id);
    User findUserByEmail(String mail);
}
