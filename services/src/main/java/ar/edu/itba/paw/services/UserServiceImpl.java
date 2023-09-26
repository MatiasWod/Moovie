package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UnableToCreateUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(String username, String email, String password){
        if(userDao.findUserByUsername(username).isPresent()){
            throw new UnableToCreateUserException("Username already in use");
        }
        Optional<User> aux = userDao.findUserByEmail(email);
        if(aux.isPresent()){
            if(aux.get().getRole() == ROLE_UNREGISTERED){
                return createUserFromUnregistered(username, email, password);
            } else{
                throw new UnableToCreateUserException("Email already in use");
            }
        }
        return userDao.createUser(username, email, passwordEncoder.encode(password));
    }

    @Override
    public User createUserFromUnregistered(String username, String email, String password) {
        return userDao.createUserFromUnregistered(username, email, passwordEncoder.encode(password));
    }

    @Override
    public Optional<User> findUserById(int userId) {
        return userDao.findUserById(userId);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public User getOrCreateUserViaMail(String mail){
        Optional<User> user = findUserByEmail(mail);
        if(user.isPresent()){
            return user.get();
        }
        return createUser(null,mail,null);
    }
}
