//import ar.edu.itba.paw.models.Cast.Actor;
//import ar.edu.itba.paw.models.Media.Media;
//import ar.edu.itba.paw.persistence.ActorDaoJdbcImpl;
//import ar.edu.itba.paw.persistence.MediaDaoJdbcImpl;
//import config.TestConfig;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class MediaDaoJdbcImplTest {
//
//    //MediaId = 2 is Barbie
//    private static final int MEDIA_ID = 2;
//    private static final String MOVIE_NAME = "Barbie";
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private MediaDaoJdbcImpl mediaDaoJdbc;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setUp(){
//        jdbcTemplate = new JdbcTemplate(ds);
//    }
//
//    @Test
//    public void testCreate(){
//        final Optional<Media> media = mediaDaoJdbc.getMediaById(MEDIA_ID);
//        assertNotNull(media);
//        assertTrue(media.isPresent());
//        assertEquals(MOVIE_NAME,media.get().getName());
//    }
//}
