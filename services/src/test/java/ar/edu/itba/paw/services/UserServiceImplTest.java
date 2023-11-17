package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.UnableToCreateUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int UID = 1;
    private static final String EMAIL = "test@mail.com";
    private static final String USERNAME = "tester";
    private static final String PASSWORD = "pass123";
    private static final int ROLE = 1;

    private User user;

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao mockUserDao;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Before
    public void setup(){
        user = new User(UID, USERNAME, EMAIL, PASSWORD, ROLE, 0);
    }

    @Test
    public void testCreateUserFromUnregistered() {
        when(mockUserDao.createUserFromUnregistered(eq(EMAIL), eq(USERNAME), eq(PASSWORD)))
                .thenReturn(user);
        when(mockPasswordEncoder.encode(Mockito.anyString())).thenReturn(PASSWORD);

        User user = userService.createUserFromUnregistered(EMAIL, USERNAME, PASSWORD);
        Assert.assertNotNull(user);
        Assert.assertEquals(UID, user.getUserId());
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(EMAIL, user.getEmail());
        Assert.assertEquals(PASSWORD, user.getPassword());
        Assert.assertEquals(ROLE, user.getRole());
    }

    @Test(expected = UnableToCreateUserException.class)
    public void testAlreadyExistingUser() throws UnableToCreateUserException{
        when(mockUserDao.findUserByUsername(USERNAME)).thenReturn(Optional.of(user)); // Mock with an existing user
        userService.createUser(USERNAME, EMAIL, PASSWORD);
        Assert.fail();
    }

}
