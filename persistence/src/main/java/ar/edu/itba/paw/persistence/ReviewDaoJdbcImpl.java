package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReviewDaoJdbcImpl implements ReviewDao {
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
    public ReviewDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        reviewJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviews").usingGeneratedKeyColumns("reviewid");
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        return jdbcTemplate.query("SELECT * FROM reviews WHERE reviewId = ?", new Object[]{reviewId}, REVIEW_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Review> getReviewsByMediaId(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM reviews WHERE mediaId = ?", new Object[]{mediaId}, REVIEW_ROW_MAPPER);
    }

    @Override
    public List<Review> getReviewForMoovieListFromUser(int moovieListId, int userId) {
        return jdbcTemplate.query("SELECT reviews.* FROM reviews INNER JOIN moovielistscontent ON moovielistscontent.mediaId = reviews.mediaId WHERE reviews.userId = ?", new Object[]{userId + " AND moovielistscontent.moovielistId = " + moovieListId}, REVIEW_ROW_MAPPER);
    }

    @Override
    public List<Review> getMovieReviewsFromUser(int userId) {
        String sql = "SELECT reviews.* FROM reviews " +
                "JOIN media ON reviews.mediaId = media.mediaId " +
                "WHERE reviews.userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, REVIEW_ROW_MAPPER);
    }

    public boolean userInMediaHasReview(int userId, int mediaId){
        Optional<Review> r = jdbcTemplate.query("SELECT * FROM reviews WHERE mediaId = ? AND userId= ? ",new Object[]{mediaId,userId},  REVIEW_ROW_MAPPER).stream().findFirst();
        if(r.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Review createReview(int userId, int mediaId, int rating, String reviewContent) {
        if(userInMediaHasReview(userId, mediaId)){
            String sqlDel = "DELETE FROM reviews WHERE userid = " + userId + " AND mediaid = " + mediaId;
            jdbcTemplate.execute(sqlDel);
        }
        if (reviewContent.isEmpty()) {
            reviewContent = null;
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("mediaId", mediaId);
        args.put("rating", rating);
        args.put("reviewLikes", 0);

        args.put("reviewContent", reviewContent);
        final Number reviewId = reviewJdbcInsert.executeAndReturnKey(args);
        return new Review(reviewId.intValue(), userId, mediaId, rating, 0, reviewContent);
    }
}