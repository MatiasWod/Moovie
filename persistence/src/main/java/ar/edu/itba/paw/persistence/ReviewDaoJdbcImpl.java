package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDaoJdbcImpl implements ReviewDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reviewJdbcInsert;

    private static final RowMapper<Review> REVIEW_ROW_MAPPER = (rs, rowNum) -> new Review(
            rs.getInt("reviewId"),
            rs.getInt("userId"),
            rs.getInt("mediaId"),
            rs.getInt("rating"),
            rs.getInt("reviewLikes"),
            rs.getString("reviewContent")
    );

    @Autowired
    public ReviewDaoJdbcImpl(final DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        reviewJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("reviews").usingGeneratedKeyColumns("reviewId");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS reviews(" +
                        "reviewId                           SERIAL PRIMARY KEY," +
                        "userId                             INTEGER NOT NULL," +
                        "mediaId                            INTEGER NOT NULL," +
                        "rating                             INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 10)," +
                        "reviewLikes                            INTEGER NOT NULL," +
                        "reviewContent                            TEXT," +
                        "FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE," +
                        "FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE," +
                        "UNIQUE(userId,mediaId))");
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        return jdbcTemplate.query("SELECT * FROM reviews WHERE reviewId = ?",new Object[]{reviewId},REVIEW_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Review> getReviewForMoovieListFromUser(int moovieListId, int userId) {
        return jdbcTemplate.query("SELECT reviews.* FROM reviews INNER JOIN moovielistscontent ON moovielistscontent.mediaId = reviews.mediaId WHERE reviews.userId = ?", new Object[]{userId+" AND moovielistscontent.moovielistId = "+moovieListId} ,REVIEW_ROW_MAPPER);
    }
}