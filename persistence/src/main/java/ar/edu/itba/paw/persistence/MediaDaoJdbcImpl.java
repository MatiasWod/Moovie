package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;


@Repository
public class MediaDaoJdbcImpl implements MediaDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert mediaJdbcInsert;
    private final SimpleJdbcInsert movieJdbcInsert;
    private final SimpleJdbcInsert tvJdbcInsert;

    private static final RowMapper<Media> MEDIA_ROW_MAPPER = (rs, rowNum) -> new Media(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status")
    );

    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = (rs, rowNum) -> new Movie(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getInt("runtime"),
            rs.getInt("budget"),
            rs.getInt("revenue"),
            rs.getInt("directorId"),
            rs.getString("director")
    );

    private static final String moviesQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, runtime, budget, revenue, directorId, director ";

    private static final RowMapper<TVSerie> TV_SERIE_ROW_MAPPER = (rs, rowNum) -> new TVSerie(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getDate("lastAirDate"),
            rs.getDate("nextEpisodeToAir"),
            rs.getInt("numberOfEpisodes"),
            rs.getInt("numberOfSeasons")
    );

    private static final String tvQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, lastAirDate, nextEpisodeToAir, numberOfEpisodes, numberOfSeasons ";


    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public MediaDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        mediaJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("media").usingGeneratedKeyColumns("mediaId");
        movieJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("movies").usingGeneratedKeyColumns("mediaId");
        tvJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tv").usingGeneratedKeyColumns("mediaId");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS media(" +
                        "mediaId                        SERIAL PRIMARY KEY," +
                        "type                           BOOLEAN NOT NULL," +
                        "name                           VARCHAR(255) NOT NULL," +
                        "originalLanguage               VARCHAR(2)," +
                        "adult                          BOOLEAN NOT NULL," +
                        "releaseDate                    DATE," +
                        "overview                       TEXT NOT NULL," +
                        "backdropPath                   VARCHAR(255)," +
                        "posterPath                     VARCHAR(255)," +
                        "trailerLink                    VARCHAR(255)," +
                        "tmdbRating                     FLOAT NOT NULL," +
                        "totalRating                    INTEGER NOT NULL," +
                        "voteCount                      INTEGER NOT NULL," +
                        "status                         VARCHAR(20) NOT NULL)");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS movies(" +
                        "mediaId                 INTEGER NOT NULL," +
                        "runtime                 INTEGER," +
                        "budget                  BIGINT," +
                        "revenue                 BIGINT," +
                        "directorId              INTEGER," +
                        "director                VARCHAR(255)," +
                        "UNIQUE(mediaId)," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE)");

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tv(" +
                        "mediaId                        INTEGER NOT NULL," +
                        "lastAirDate                    DATE," +
                        "nextEpisodeToAir               DATE," +
                        "numberOfEpisodes               INTEGER," +
                        "numberOfSeasons                INTEGER," +
                        "UNIQUE(mediaId)," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE)");
    }

    @Override
    public Optional<Media> getMediaById(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM media WHERE mediaId = ?",new Object[]{mediaId},MEDIA_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Media> getMediaList() {
        return jdbcTemplate.query("SELECT * FROM media", MEDIA_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getMediaCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM media", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return jdbcTemplate.query("SELECT " +moviesQueryParams+ " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid WHERE  movies.mediaid = ?",new Object[]{mediaId},MOVIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Movie> getMovieList() {
        return jdbcTemplate.query("SELECT " +moviesQueryParams+ " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid", MOVIE_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM movies", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return jdbcTemplate.query("SELECT " +tvQueryParams+  " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid WHERE  tv.mediaid = ?",new Object[]{mediaId},TV_SERIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<TVSerie> getTvList() {
        return jdbcTemplate.query("SELECT " +tvQueryParams+ " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid", TV_SERIE_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getTvCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM tv", COUNT_ROW_MAPPER).stream().findFirst();
    }

    /*
    @Override
    public List<Media> getMediaOrderedByTmdbRating() {
        return jdbcTemplate.query("SELECT * FROM media ORDER BY tmdbrating DESC", MEDIA_ROW_MAPPER);
    }

    @Override
    public List<TVSerie> getTvOrderedByTmdbRating() {
        return jdbcTemplate.query("SELECT * FROM media ORDER BY tmdbrating DESC", MEDIA_ROW_MAPPER);
    }


     */
}
