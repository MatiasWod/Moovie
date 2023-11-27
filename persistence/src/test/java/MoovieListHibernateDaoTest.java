import ar.edu.itba.paw.models.MoovieList.MoovieList;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Arrays;
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

    private static final int INSERTED_MOOVIELIST_ID =2;

    private static final int TO_INSERT_MEDIALIST = 2;

    private static final List<Integer> TO_INSERT_MEDIA_LIST = Arrays.asList(1, 2, 3, 4, 5);

    private static final int TO_INSERT_USER_ID = 5;
    private static final String MOOVIELIST_TABLE = "moovielists";

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




}
