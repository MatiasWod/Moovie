package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MediaList.MediaListContent;
import ar.edu.itba.paw.models.Rating.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class RatingDaoJdbcImpl implements RatingDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert ratingJdbcInsert;

    private static final RowMapper<Rating> RATING_ROW_MAPPER = (rs, rowNum) -> new Rating(
            rs.getInt("ratingId"),
            rs.getInt("userId"),
            rs.getInt("mediaId"),
            rs.getInt("score")
    );

    @Autowired
    public RatingDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        ratingJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("ratings").usingGeneratedKeyColumns("ratingId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS ratings(" +
                        "ratingId                           SERIAL PRIMARY KEY," +
                        "userId                             INTEGER NOT NULL," +
                        "mediaId                            INTEGER NOT NULL," +
                        "score                              INTEGER NOT NULL CHECK(score BETWEEN 1 AND 10)," +
                        "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE," +
                        "UNIQUE(userId,mediaId))");
    }

    @Override
    public Optional<Rating> getRatingById(int ratingId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE ratingId = ?",new Object[]{ratingId},RATING_ROW_MAPPER).stream().findFirst();

    }
}
