/*import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.persistence.GenreDaoJdbcImpl;
import ar.edu.itba.paw.persistence.ProviderDaoJdbcImpl;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class GenreDaoJdbcImplTest {
    private static final int MEDIA_ID = 190;
    private static final String[] GENRES_NAME = {"Action & Adventure","Sci-Fi & Fantasy"};

    @Autowired
    private DataSource ds;

    @Autowired
    private GenreDaoJdbcImpl genreDaoJdbc;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGenres(){
        final List<Genre> genres = genreDaoJdbc.getGenreForMedia(MEDIA_ID);
        assertNotNull(genres);
        assertEquals(GENRES_NAME.length, genres.size());
        for (int i = 0 ; i < genres.size() ; i++){
            assertEquals(genres.get(i).getGenre(),GENRES_NAME[i]);
        }
    }
}
*/