import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ActorDaoJdbcImpl;
import ar.edu.itba.paw.persistence.MediaDaoJdbcImpl;
import ar.edu.itba.paw.persistence.MoovieListDaoJdbcImpl;
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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MoovieListDaoJdbcImplTest {

    private static final String EMAIL = "asd@email.com";

    @Autowired
    private DataSource ds;

    @Autowired
    private MoovieListDaoJdbcImpl moovieListDaoJdbc;

    @Autowired
    private UserDaoJdbcImpl userDaoJdbc;

    private JdbcTemplate jdbcTemplate;

    private static final String TEST_EMAIL = "a@email.com";
    private static final String MOOVIE_LIST_NAME = "testList";

    private static final String MOOVIE_LIST_DESCRIPTION = "moovieListDescription";
    private static final List<Integer> mediaIdList = new ArrayList<>();

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"moovieLists");
    }

    @Test
    public void testCreate(){
        final User user = userDaoJdbc.createUser(TEST_EMAIL);
        final MoovieList moovieList = moovieListDaoJdbc.createMoovieList(user.getUserId(),MOOVIE_LIST_NAME,MOOVIE_LIST_DESCRIPTION);
        assertNotNull(moovieList);
        assertEquals(MOOVIE_LIST_NAME,moovieList.getName());
        assertEquals(MOOVIE_LIST_DESCRIPTION,moovieList.getDescription());
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,"moovieLists"));
        mediaIdList.add(1);
        mediaIdList.add(2);
        final MoovieList moovieListWithContent = moovieListDaoJdbc
                .createMoovieListWithContent(user.getUserId(), MOOVIE_LIST_NAME,
                        MOOVIE_LIST_DESCRIPTION,mediaIdList);
        assertNotNull(moovieListWithContent);
        assertEquals(MOOVIE_LIST_NAME,moovieListWithContent.getName());
        assertEquals(MOOVIE_LIST_DESCRIPTION,moovieListWithContent.getDescription());
        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate,"moovieLists"));
        mediaIdList.add(3);
        moovieListDaoJdbc.insertMediaIntoMoovieList(moovieListWithContent.getMoovieListId(),mediaIdList);
        assertTrue(moovieListDaoJdbc.getMoovieListCount().isPresent());
        assertEquals(2,moovieListDaoJdbc.getMoovieListCount());
    }
}
