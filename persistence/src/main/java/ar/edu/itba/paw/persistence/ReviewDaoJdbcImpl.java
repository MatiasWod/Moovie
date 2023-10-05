package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.Review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
            rs.getString("username"),
            rs.getInt("mediaId"),
            rs.getInt("rating"),
            rs.getInt("reviewLikes"),
            rs.getString("name"),
            rs.getString("posterpath"),
            rs.getString("reviewContent")
    );

    @Autowired
    public ReviewDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        reviewJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviews").usingGeneratedKeyColumns("reviewid");
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        return jdbcTemplate.query("SELECT * FROM reviews INNER JOIN users ON users.userid = reviews.userid INNER JOIN media ON media.mediaId = reviews.mediaId WHERE reviews.reviewId = ?", new Object[]{reviewId}, REVIEW_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Review> getReviewsByMediaId(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM reviews INNER JOIN users ON users.userid = reviews.userid INNER JOIN media ON media.mediaId = reviews.mediaId WHERE reviews.mediaId = ?", new Object[]{mediaId}, REVIEW_ROW_MAPPER);
    }


    @Override
    public List<Review> getMovieReviewsFromUser(int userId) {
        String sql = "SELECT reviews.* FROM reviews " +
                "JOIN media ON reviews.mediaId = media.mediaId " +
                "WHERE reviews.userId = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, REVIEW_ROW_MAPPER);
    }


    @Override
    public void createReview(int userId, int mediaId, int rating, String reviewContent) {
        if (reviewContent.isEmpty()) {
            reviewContent = null;
        }
        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("mediaId", mediaId);
        args.put("rating", rating);
        args.put("reviewLikes", 0);

        args.put("reviewContent", reviewContent);
        try{
            reviewJdbcInsert.executeAndReturnKey(args);
        } catch(DuplicateKeyException e){
            throw new UnableToInsertIntoDatabase("Create review failed, already have a review in this movie");
        }

    }

    @Override
    public void deleteReview(int reviewId) {
        String sqlDel = "DELETE FROM reviews WHERE reviewId = " + reviewId;
        jdbcTemplate.execute(sqlDel);
    }
}