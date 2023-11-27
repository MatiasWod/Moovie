import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
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
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
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

    private static final int INSERTED_MOOVIELIST_ID = 1;
    private static final int TO_INSERT_USER_ID = 95;
    private static final String MOOVIELIST_TABLE = "moovielists";
    private static final String MOOVIELISTCONTENT_TABLE = "moovielistscontent";

    private static final String MOOVIELIST_NAME = "Namenamename";
    private static final int MOOVIE_LIST_ID = 1;

    private static final int ID_TO_DELETE = 2;

    private static final int MEDIA_IN_MOOVIE_LIST = 1;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Rollback
    @Test
    public void testGetMoovieListById(){
        Optional<MoovieList> moovieList = moovieListHibernateDao.getMoovieListById(INSERTED_MOOVIELIST_ID);
        Assert.assertTrue(moovieList.isPresent());
        Assert.assertEquals(INSERTED_MOOVIELIST_ID,moovieList.get().getMoovieListId());
    }

    /*@Rollback
    @Test
    public void testCreateMoovieList() {
        MoovieList ml = moovieListHibernateDao.createMoovieList(TO_INSERT_USER_ID, MOOVIELIST_NAME, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), "Description");
        entityManager.flush();

        Assert.assertNotNull(ml);
        Assert.assertEquals(ml.getName(), MOOVIELIST_NAME);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, MOOVIELIST_TABLE, String.format("name = '%s'", MOOVIELIST_NAME)));
    }*/

    @Rollback
    @Test
    public void testGetFeaturedMoovieListContent(){
        List<Media> m = moovieListHibernateDao.getFeaturedMoovieListContent( MediaTypes.TYPE_ALL.getType(), -1, "tmdbRating", "tmdbRating", "desc", 2, 0 );

        Assert.assertEquals(m.get(0).getMediaId(), 91);
        Assert.assertEquals(m.get(1).getMediaId(), 247);
    }


    @Rollback
    @Test
    public void testDeleteMoovieList(){
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELIST_TABLE));

        moovieListHibernateDao.deleteMoovieList(ID_TO_DELETE);

        entityManager.flush();

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELIST_TABLE));
    }

    @Rollback
    @Test
    public void testDeleteMediaFromMoovieList(){
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELISTCONTENT_TABLE));

        moovieListHibernateDao.deleteMediaFromMoovieList(MOOVIE_LIST_ID, MEDIA_IN_MOOVIE_LIST);

        entityManager.flush();

        Assert.assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, MOOVIELISTCONTENT_TABLE));
    }


}
