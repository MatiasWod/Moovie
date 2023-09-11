package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class TVSerieDaoJdbcImpl implements TVSerieDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<TVSerie> TV_SERIE_ROW_MAPPER = (rs, rowNum) -> new TVSerie(
            rs.getInt("tvId"),
            rs.getString("tvName"),
            rs.getDate("release_date"),
            rs.getDate("lastAirDate"),
            rs.getDate("nextEpisodeToAir"),
            rs.getString("originalLang"),
            rs.getBoolean("adult"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getInt("numberOfEpisodes"),
            rs.getInt("numberOfSeasons")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public TVSerieDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("tv").usingGeneratedKeyColumns("tvId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tv(" +
                        "tvId                           SERIAL PRIMARY KEY," +
                        "tvName                         VARCHAR(255) NOT NULL," +
                        "releaseDate                    DATE NOT NULL," +
                        "lastAirDate                    DATE NOT NULL," +
                        "nextEpisodeToAir               DATE NOT NULL," +
                        "runtime                        INTEGER NOT NULL," +
                        "originalLanguage               VARCHAR(2)," +
                        "adult                          BOOLEAN NOT NULL," +
                        "overview                       TEXT NOT NULL," +
                        "backdropPath                   VARCHAR(255)," +
                        "posterPath                     VARCHAR(255)," +
                        "trailerLink                    VARCHAR(255)," +
                        "totalRating                    INTEGER NOT NULL," +
                        "voteCount                      INTEGER NOT NULL," +
                        "status                         VARCHAR(20) NOT NULL," +
                        "numberOfEpisodes               INTEGER NOT NULL," +
                        "numberOfSeasons                INTEGER NOT NULL)" );
    }

    @Override
    public Optional<TVSerie> getById(int tvId) {
        return jdbcTemplate.query("SELECT * FROM tv WHERE tvId = ?",new Object[]{tvId},TV_SERIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<TVSerie> getTvList() {
        return jdbcTemplate.query("SELECT * FROM tv", TV_SERIE_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getTvCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM tv", COUNT_ROW_MAPPER).stream().findFirst();
    }
}
