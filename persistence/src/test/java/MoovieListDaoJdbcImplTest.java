import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.persistence.MoovieListDaoJdbcImpl;
import ar.edu.itba.paw.persistence.UserDaoJdbcImpl;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    private static final int EXISTING_USERNAME_ID = 1;
    private static final int EXISTING_MOOVIE_LIST_ID = 1;
    private static final int EXISTING_MOOVIE_LIST_LIKED_ID = 2;
    private static final int EXISTING_MOOVIE_LIST_FOLLOWED_ID = 2;

    private static final int EXISTING_MOOVIE_LIST_USER_ID = 1;
    private static final String EXISTING_MOOVIE_LIST_NAME = "CavaniList";
    private static final String EXISTING_MOOVIE_LIST_DESCRIPTION = "CavaniListDescription";
    private static final int EXISTING_MOOVIE_LIST_TYPE = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();
    private static final List<Integer> EXISTING_MEDIA_ID_LIST = new ArrayList<Integer>(Collections.singletonList(1));

    private static final int TO_DELETE_MOOVIE_LIST_ID = 2;
    private static final int TO_DELETE_MEDIA_ID = 1;

    private static final String TO_CREATE_MOOVIE_LIST_NAME = "testList";
    private static final String TO_CREATE_MOOVIE_LIST_DESCRIPTION = "moovieListDescription";
    private static final int TO_CREATE_MOOVIE_LIST_TYPE = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Rollback
    @Test
    public void testCreateMoovieList() {
        final MoovieList moovieList = moovieListDaoJdbc.createMoovieList(EXISTING_USERNAME_ID, TO_CREATE_MOOVIE_LIST_NAME, TO_CREATE_MOOVIE_LIST_TYPE, TO_CREATE_MOOVIE_LIST_DESCRIPTION);
        assertNotNull(moovieList);
        assertEquals(TO_CREATE_MOOVIE_LIST_NAME, moovieList.getName());
        assertEquals(TO_CREATE_MOOVIE_LIST_DESCRIPTION, moovieList.getDescription());
        assertEquals(TO_CREATE_MOOVIE_LIST_TYPE, moovieList.getType());
        assertEquals(EXISTING_USERNAME_ID, moovieList.getUserId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieLists", "moovieListId = " + moovieList.getMoovieListId()));
    }

    @Rollback
    @Test
    public void testGetMoovieList() {
        final MoovieList moovieList = moovieListDaoJdbc.getMoovieListById(EXISTING_MOOVIE_LIST_ID).orElse(null);
        assertNotNull(moovieList);
        assertEquals(EXISTING_MOOVIE_LIST_ID, moovieList.getMoovieListId());
        assertEquals(EXISTING_MOOVIE_LIST_NAME, moovieList.getName());
        assertEquals(EXISTING_MOOVIE_LIST_DESCRIPTION, moovieList.getDescription());
        assertEquals(EXISTING_MOOVIE_LIST_TYPE, moovieList.getType());
        assertEquals(EXISTING_MOOVIE_LIST_USER_ID, moovieList.getUserId());
    }

    @Rollback
    @Test
    public void testInsertToMoovieList() {
        moovieListDaoJdbc.insertMediaIntoMoovieList(EXISTING_MOOVIE_LIST_ID, EXISTING_MEDIA_ID_LIST);
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsContent", "moovieListId = " + EXISTING_MOOVIE_LIST_ID));
    }

    @Rollback
    @Test
    public void testDeleteMediaFromMoovieList() {
        moovieListDaoJdbc.deleteMediaFromMoovieList(EXISTING_MOOVIE_LIST_ID, TO_DELETE_MEDIA_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsContent", "moovieListId = " + EXISTING_MOOVIE_LIST_ID));
    }

    @Rollback
    @Test
    public void testDeleteMoovieList() {
        moovieListDaoJdbc.deleteMoovieList(TO_DELETE_MOOVIE_LIST_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieLists", "moovieListId = " + TO_DELETE_MOOVIE_LIST_ID));
    }

    @Rollback
    @Test
    public void testLikeMoovieList() {
        moovieListDaoJdbc.likeMoovieList(EXISTING_USERNAME_ID, EXISTING_MOOVIE_LIST_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsLikes", "moovieListId = " + EXISTING_MOOVIE_LIST_ID));
    }

    @Rollback
    @Test
    public void testRemoveLikeMoovieList() {
        moovieListDaoJdbc.removeLikeMoovieList(EXISTING_USERNAME_ID, EXISTING_MOOVIE_LIST_LIKED_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsLikes", "moovieListId = " + EXISTING_MOOVIE_LIST_LIKED_ID));
    }

    @Rollback
    @Test
    public void testFollowMoovieList(){
        moovieListDaoJdbc.followMoovieList(EXISTING_USERNAME_ID, EXISTING_MOOVIE_LIST_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsfollows", "moovieListId = " + EXISTING_MOOVIE_LIST_ID));
    }

    @Rollback
    @Test
    public void testRemoveFollowMoovieList(){
        moovieListDaoJdbc.removeFollowMoovieList(EXISTING_USERNAME_ID, EXISTING_MOOVIE_LIST_FOLLOWED_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "moovieListsfollows", "moovieListId = " + EXISTING_MOOVIE_LIST_FOLLOWED_ID));
    }
}
