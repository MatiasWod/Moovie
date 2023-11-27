import ar.edu.itba.paw.exceptions.UnableToCreateUserException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserHibernateDao;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserHibernateDaoTest {
    @Autowired
    private UserHibernateDao userHibernateDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    private static final int INSERTED_USER_ID = 1;
    private static final String INSERTED_USER_EMAIL = "cavani@test.com";
    private static final String INSERTED_USER_USERNAME = "Cavani";

    private static final int TO_INSERT_USER_ID = 4;
    private static final String TO_INSERT_USER_EMAIL = "moovie@test.com";
    private static final String TO_INSERT_USER_USERNAME = "testUser";
    private static final String TO_INSERT_USER_PASSWORD = "pass123";
    private static final String USERS_TABLE = "users";
    private static final int NON_EXISTENT_USER_ID = 5;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testGetUserById(){
        final Optional<User> user = userHibernateDao.findUserById(INSERTED_USER_ID);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_EMAIL,user.get().getEmail());
    }

    @Rollback
    @Test
    public void testGetUserByEmail(){
        final Optional<User> user = userHibernateDao.findUserByEmail(INSERTED_USER_EMAIL);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_ID,user.get().getUserId());
    }

    @Rollback
    @Test
    public void testGetUserByUsername(){
        final Optional<User> user = userHibernateDao.findUserByUsername(INSERTED_USER_USERNAME);

        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(INSERTED_USER_ID,user.get().getUserId());
    }

    @Rollback
    @Test
    public void testCreateUser() {
        final User user = userHibernateDao.createUser(TO_INSERT_USER_USERNAME,TO_INSERT_USER_EMAIL,TO_INSERT_USER_PASSWORD);

        entityManager.flush();

        Assert.assertNotNull(user);
        Assert.assertEquals(TO_INSERT_USER_EMAIL, user.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("email = '%s'", TO_INSERT_USER_EMAIL)));
    }

    @Rollback
    @Test(expected = UnableToCreateUserException.class)
    public void testUnableToCreateUser() throws UnableToCreateUserException{

        userHibernateDao.createUser(TO_INSERT_USER_EMAIL, INSERTED_USER_USERNAME,TO_INSERT_USER_PASSWORD);

        //Assert.fail();
        //Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("email = '%s'", INSERTED_USER_EMAIL)));
    }

    @Rollback
    @Test(expected = UnableToFindUserException.class)
    public void testUnableToFindUser() throws UnableToFindUserException {

        userHibernateDao.findUserById(NON_EXISTENT_USER_ID);

        Assert.fail();
        Assert.assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USERS_TABLE, String.format("userId = '%d'", INSERTED_USER_ID)));
    }
}
