import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDaoJdbcImpl;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoJdbcImplTest {

    private static final int EXISTING_USER_ID = 1;
    private static final String EXISTING_USERNAME = "Cavani";
    private static final String EXISTING_EMAIL = "cavani@test.com";

    private static final String USERNAME = "SteveAoki";
    private static final String EMAIL = "asd@email.com";
    private static final String PASSWORD = "pass123";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserDaoJdbcImpl userDaoJdbc;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate(){
        final User user = userDaoJdbc.createUser(USERNAME, EMAIL, PASSWORD);
        assertNotNull(user);
        assertEquals(EMAIL,user.getEmail());
    }

    @Test
    public void testGetUser(){
        final Optional<User> user = userDaoJdbc.findUserById(EXISTING_USER_ID);

        assertTrue(user.isPresent());
        assertEquals(user.get().getUsername(), EXISTING_USERNAME);
        assertEquals(user.get().getEmail(), EXISTING_EMAIL);
    }

}
