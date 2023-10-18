package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int UID = 1;
    private static final String EMAIL = "test@mail.com";
    private static final String USERNAME = "tester";
    private static final String PASS = "pass123";
    private static final int ROLE = 1;

    private User user;

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao mockUserDao;
    @Mock
    private MoovieListDao mocMoovieListDao;
    @Mock
    private EmailService mockEmailService;

    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private MessageSource mockMessageSource;
    @Mock
    private VerificationTokenService mockVerificationTokenService;

    @Before
    public void setup(){
        user = new User(UID,USERNAME,EMAIL,PASS,ROLE);
    }


}
