package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User createUser(String email) {
        return userDao.createUser(email);
    }

    @Override
    public User findUserById(int userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }
}
