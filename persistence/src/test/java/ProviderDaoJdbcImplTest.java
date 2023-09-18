//import ar.edu.itba.paw.models.Provider.Provider;
//import ar.edu.itba.paw.models.TV.TVCreators;
//import ar.edu.itba.paw.persistence.ProviderDaoJdbcImpl;
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
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class ProviderDaoJdbcImplTest {
//
//    //MediaId = 190 is ONE PIECE
//    private static final int MEDIA_ID = 190;
//    private static final String[] PROVIDERS_NAME = {"Netflix"};
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private ProviderDaoJdbcImpl providerDaoJdbc;
//
//    private JdbcTemplate jdbcTemplate;
//
//    @Before
//    public void setUp(){
//        jdbcTemplate = new JdbcTemplate(ds);
//    }
//
//    @Test
//    public void testProviders(){
//        final List<Provider> providers = providerDaoJdbc.getProviderForMedia(MEDIA_ID);
//        assertNotNull(providers);
//        assertEquals(PROVIDERS_NAME.length, providers.size());
//        for (int i = 0 ; i < providers.size() ; i++){
//            assertEquals(providers.get(i).getProviderName(),PROVIDERS_NAME[i]);
//        }
//    }
//}