/*import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.persistence.ActorDaoJdbcImpl;
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

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ActorDaoJdbcImplTest {

    private static final int MEDIA_ID = 1;
    private static final int NUMBER_OF_ACTORS = 10;
    private static final int ACTOR_ID=976;
    private static final String ACTOR_NAME="Jason Statham";
    private static final String CHARACTER_NAME="Jonas Taylor";


    @Autowired
    private DataSource ds;

    @Autowired
    private ActorDaoJdbcImpl actorDaoJdbc;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"actors");
    }

    @Test
    public void testActor(){
        final List<Actor> actors = actorDaoJdbc.getAllActorsForMedia(MEDIA_ID);
        assertNotNull(actors);
        assertEquals(NUMBER_OF_ACTORS,actors.size());
        assertEquals(NUMBER_OF_ACTORS,JdbcTestUtils.countRowsInTable(jdbcTemplate,"actors"));
        assertEquals(actors.get(ACTOR_ID).getActorName(),ACTOR_NAME);
        assertEquals(actors.get(ACTOR_ID).getCharacterName(),CHARACTER_NAME);
    }
}
*/