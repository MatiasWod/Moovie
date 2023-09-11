package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Cast.MovieActor;
import ar.edu.itba.paw.models.Cast.TVActor;
import ar.edu.itba.paw.models.Movie.Movie;
import ar.edu.itba.paw.services.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ActorDaoJdbcImpl implements ActorDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert tvCastjdbcInsert;
    private final SimpleJdbcInsert movieCastjdbcInsert;

    private static final RowMapper<MovieActor> MOVIE_ACTOR_ROW_MAPPER = (rs, rowNum) -> new MovieActor(
            rs.getInt("movieId"),
            rs.getString("actorName"),
            rs.getString("characterName"),
            rs.getString("profilePath")
    );

    private static final RowMapper<TVActor> TV_ACTOR_ROW_MAPPER = (rs, rowNum) -> new TVActor(
            rs.getInt("tvId"),
            rs.getString("actorName"),
            rs.getString("characterName"),
            rs.getString("profilePath")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public ActorDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        tvCastjdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tvCast").usingGeneratedKeyColumns("tvId");
        movieCastjdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("movieCast").usingGeneratedKeyColumns("movieId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tvCast(" +
                        "tvId                    SERIAL PRIMARY KEY," +
                        "actorName               VARCHAR(100) NOT NULL," +
                        "characterName           VARCHAR(100)," +
                        "profilePath             VARCHAR(255)," +
                        "FOREIGN KEY(tvId)       REFERENCES tv(tvId) ON DELETE CASCADE)");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS movieCast(" +
                        "movieId                    SERIAL PRIMARY KEY," +
                        "actorName                  VARCHAR(100) NOT NULL," +
                        "characterName              VARCHAR(100)," +
                        "profilePath                VARCHAR(255)," +
                        "FOREIGN KEY(tvId)          REFERENCES tv(tvId) ON DELETE CASCADE)");
    }

    @Override
    public List<MovieActor> getAllActorsForMovie(int movieId) {
        return jdbcTemplate.query("SELECT * FROM movies WHERE movieId = ?",new Object[]{movieId},MOVIE_ACTOR_ROW_MAPPER);
    }

    @Override
    public List<TVActor> getAllActorsForTvSerie(int tvId) {
        return jdbcTemplate.query("SELECT * FROM tv WHERE tvId = ?",new Object[]{tvId},TV_ACTOR_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getNumberOfMovies() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getNumberOfTvSeries() {
        return Optional.empty();
    }
}
