package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.MovieActor;
import ar.edu.itba.paw.models.Cast.TVActor;
import ar.edu.itba.paw.models.Genre.MovieGenre;
import ar.edu.itba.paw.models.Genre.TVGenre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class GenreDaoJdbcImpl implements GenreDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert tvGenrejdbcInsert;
    private final SimpleJdbcInsert movieGenrejdbcInsert;

    private static final RowMapper<MovieGenre> MOVIE_GENRE_ROW_MAPPER = (rs, rowNum) -> new MovieGenre(
            rs.getInt("movieId"),
            rs.getString("genre")
    );

    private static final RowMapper<TVGenre> TV_GENRE_ROW_MAPPER = (rs, rowNum) -> new TVGenre(
            rs.getInt("tvId"),
            rs.getString("genre")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public GenreDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        tvGenrejdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tvGenre").usingGeneratedKeyColumns("tvId");
        movieGenrejdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("movieGenre").usingGeneratedKeyColumns("movieId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tvGenre(" +
                        "tvId                    INTEGER NOT NULL," +
                        "genre                   VARCHAR(100) NOT NULL," +
                        "PRIMARY KEY(tvId))");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS movieGenre(" +
                        "movieId                    INTEGER NOT NULL," +
                        "genre                      VARCHAR(100) NOT NULL," +
                        "PRIMARY KEY(movieId))");
    }

    @Override
    public Optional<MovieGenre> getGenreForMovie(int movieId) {
        //revisar el findFirst, creo que siempre devuelve el primer género que encuentre que matchea con el movieId
        return jdbcTemplate.query("SELECT * FROM movies WHERE movieId = ?",new Object[]{movieId},MOVIE_GENRE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<TVGenre> getGenreForTvSerie(int tvId) {
        //revisar el findFirst, creo que siempre devuelve el primer género que encuentre que matchea con el tvId
        return jdbcTemplate.query("SELECT * FROM tv WHERE tvId = ?",new Object[]{tvId},TV_GENRE_ROW_MAPPER).stream().findFirst();
    }
}
