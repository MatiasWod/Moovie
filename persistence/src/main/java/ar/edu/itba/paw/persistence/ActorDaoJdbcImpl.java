package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ActorDaoJdbcImpl implements ActorDao {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Actor> ACTOR_ROW_MAPPER = (rs, rowNum) -> new Actor(
            rs.getInt("mediaId"),
            rs.getInt("actorId"),
            rs.getString("actorName"),
            rs.getString("characterName"),
            rs.getString("profilePath")
    );


    @Autowired
    public ActorDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Actor> getAllActorsForMedia(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM actors WHERE mediaId = ?", new Object[]{mediaId}, ACTOR_ROW_MAPPER);
    }

}
