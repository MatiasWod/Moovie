import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListHibernateDao;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MoovieListHibernateDaoTest {

    @Autowired
    private MoovieListHibernateDao moovieListHibernateDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    private static final int INSERTED_MOOVIELIST_ID = 2;
    private static final int TO_INSERT_USER_ID = 95;

    private static final int TO_INSERT_MEDIALIST = 3;

    private static final String MOOVIELIST_TABLE = "moovielists";
    private static final String MOOVIELISTCONTENT_TABLE = "moovielistscontent";

    private static final String MOOVIELIST_NAME = "CavaniList";
    private static final String MOOVIELIST_DESCRIPTION = "CavaniListDescription";
    private static final int MOOVIE_LIST_ID = 1;

    private static final int ID_TO_DELETE = 2;

    private static final int MEDIA_IN_MOOVIE_LIST = 9;
    private static final int MEDIA_NOT_IN_MOOVIE_LIST = 23;
    private static final int MOOVIELIST_FOLLOWERS = 1;
    private static final String NEW_MOOVIELIST_NAME = "New List Name";
    private static final String NEW_MOOVIELIST_DESCRIPTION = "New List Description";

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Rollback
    @Test
    public void testGetMoovieListById(){
        Optional<MoovieList> moovieList = moovieListHibernateDao.getMoovieListById(INSERTED_MOOVIELIST_ID);

        Assert.assertNotNull(moovieList);
        Assert.assertEquals(INSERTED_MOOVIELIST_ID, moovieList.get().getMoovieListId());
    }

    @Rollback
    @Test
    public void testGetMoovieListFollowers(){
        List<User> userList = moovieListHibernateDao.getMoovieListFollowers(INSERTED_MOOVIELIST_ID);

        Assert.assertNotNull(userList);
        Assert.assertEquals(MOOVIELIST_FOLLOWERS, userList.size());
    }

    @Rollback
    @Test
    public void testIsMediaInMoovieList(){
        int isMediaInMoovieList = moovieListHibernateDao.isMediaInMoovieList(MEDIA_IN_MOOVIE_LIST, INSERTED_MOOVIELIST_ID);
        Assert.assertNotEquals(-1,isMediaInMoovieList);

        int isMediaNotInMoovieList = moovieListHibernateDao.isMediaInMoovieList(MEDIA_NOT_IN_MOOVIE_LIST, INSERTED_MOOVIELIST_ID);
        Assert.assertEquals(-1,isMediaNotInMoovieList);
    }

    @Rollback
    @Test
    public void testInsertMediaIntoMoovieList(){
        List<Integer> toInsert = new ArrayList<>();
        toInsert.add(MEDIA_NOT_IN_MOOVIE_LIST);
        moovieListHibernateDao.insertMediaIntoMoovieList(INSERTED_MOOVIELIST_ID, toInsert);
        entityManager.flush();
        int isMediaInMoovieList = moovieListHibernateDao.isMediaInMoovieList(MEDIA_NOT_IN_MOOVIE_LIST, INSERTED_MOOVIELIST_ID);
        Assert.assertNotEquals(-1,isMediaInMoovieList);
    }

    @Rollback
    @Test
    public void testEditMoovieList(){
        Assert.assertEquals(MOOVIELIST_NAME, moovieListHibernateDao.getMoovieListById(INSERTED_MOOVIELIST_ID).get().getName());
        Assert.assertEquals(MOOVIELIST_DESCRIPTION, moovieListHibernateDao.getMoovieListById(INSERTED_MOOVIELIST_ID).get().getDescription());
        moovieListHibernateDao.editMoovieList(INSERTED_MOOVIELIST_ID, NEW_MOOVIELIST_NAME, NEW_MOOVIELIST_DESCRIPTION);
        entityManager.flush();
        Optional<MoovieList> moovieList = moovieListHibernateDao.getMoovieListById(INSERTED_MOOVIELIST_ID);
        Assert.assertNotNull(moovieList);
        Assert.assertEquals(NEW_MOOVIELIST_NAME, moovieList.get().getName());
        Assert.assertEquals(NEW_MOOVIELIST_DESCRIPTION, moovieList.get().getDescription());
    }

    @Rollback
    @Test
    public void testDeleteMediaFromMoovieList(){
        Assert.assertNotEquals(-1,moovieListHibernateDao.isMediaInMoovieList(MEDIA_IN_MOOVIE_LIST, INSERTED_MOOVIELIST_ID));
        moovieListHibernateDao.deleteMediaFromMoovieList(INSERTED_MOOVIELIST_ID, MEDIA_IN_MOOVIE_LIST);
        entityManager.flush();
        int isMediaInMoovieList = moovieListHibernateDao.isMediaInMoovieList(MEDIA_IN_MOOVIE_LIST, INSERTED_MOOVIELIST_ID);
        Assert.assertEquals(-1,isMediaInMoovieList);
    }

    @Rollback
    @Test
    public void testDeleteMoovieList(){
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELIST_TABLE));

        moovieListHibernateDao.deleteMoovieList(ID_TO_DELETE);

        entityManager.flush();

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELIST_TABLE));
    }

}
