import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDaoJdbcImpl;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoJdbcImplTest {
    private static final String EMAIL = "asd@email.com";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserDaoJdbcImpl userDaoJdbc;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"users");
    }

    @Test
    public void testCreate(){
        final User user = userDaoJdbc.createUser(EMAIL);
        assertNotNull(user);
        assertEquals(EMAIL,user.getEmail());
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,"users"));
    }
}
