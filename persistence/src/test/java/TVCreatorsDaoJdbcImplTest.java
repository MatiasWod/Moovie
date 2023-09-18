//import ar.edu.itba.paw.models.TV.TVCreators;
//import ar.edu.itba.paw.persistence.TVCreatorsDaoJdbcImpl;
//import config.TestConfig;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.sql.DataSource;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class TVCreatorsDaoJdbcImplTest {
//
//    //MediaId = 190 is ONE PIECE
//    private static final int MEDIA_ID = 190;
//    private static final String[] CREATORS_NAME = {"Steven Maeda"};
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private TVCreatorsDaoJdbcImpl tvCreatorsDaoJdbc;
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
//        final List<TVCreators> tvCreators = tvCreatorsDaoJdbc.getTvCreatorsByMediaId(MEDIA_ID);
//        assertNotNull(tvCreators);
//        assertEquals(CREATORS_NAME.length, tvCreators.size());
//        for (int i = 0 ; i < tvCreators.size() ; i++){
//            assertEquals(tvCreators.get(i).getCreatorName(),CREATORS_NAME[i]);
//        }
//    }
//}