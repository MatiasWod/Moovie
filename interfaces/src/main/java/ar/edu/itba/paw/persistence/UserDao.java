package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User.User;

public interface UserDao {
    User createUser(String email);

    User findUserById(int id);
    User findUserByEmail(String email);
}
