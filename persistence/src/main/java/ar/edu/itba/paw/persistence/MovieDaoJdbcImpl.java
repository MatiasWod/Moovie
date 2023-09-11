package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Movie.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieDaoJdbcImpl implements MovieDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = (rs,rowNum) -> new Movie(
            rs.getInt("movieId"),
            rs.getString("movieName"),
            rs.getDate("releaseDate"),
            rs.getInt("runtime"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getInt("budget"),
            rs.getInt("revenue"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public MovieDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("movies").usingGeneratedKeyColumns("movieId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS movies(" +
                        "movieId                 SERIAL PRIMARY KEY," +
                        "movieName               VARCHAR(255) NOT NULL," +
                        "releaseDate             DATE NOT NULL," +
                        "runtime                 INTEGER NOT NULL," +
                        "originalLanguage        VARCHAR(2)," +
                        "adult                   BOOLEAN NOT NULL," +
                        "overview                TEXT NOT NULL," +
                        "backdropPath            VARCHAR(255)," +
                        "posterPath              VARCHAR(255)," +
                        "trailerLink             VARCHAR(255)," +
                        "budget                  INTEGER," +
                        "revenue                 INTEGER," +
                        "totalRating             INTEGER NOT NULL," +
                        "voteCount               INTEGER NOT NULL," +
                        "status                  VARCHAR(20) NOT NULL)");
    }

    @Override
    public Optional<Movie> getById(int movieId) {
        return jdbcTemplate.query("SELECT * FROM movies WHERE movieId = ?",new Object[]{movieId},MOVIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM movies", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Movie> getMovieList() {
        return jdbcTemplate.query("SELECT * FROM movies", MOVIE_ROW_MAPPER);
    }
}
