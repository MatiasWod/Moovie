package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.LikingFailedException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ReviewDaoJdbcImpl implements ReviewDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reviewJdbcInsert;
    private final SimpleJdbcInsert reviewLikesJdbcInsert;

    private static final RowMapper<Review> REVIEW_ROW_MAPPER = (rs, rowNum) -> new Review(
            rs.getInt("reviewId"),
            rs.getInt("userId"),
            rs.getString("username"),
            rs.getInt("mediaId"),
            rs.getInt("rating"),
            rs.getInt("reviewLikes"),
            rs.getBoolean("hasLiked"),
            rs.getString("name"),
            rs.getString("posterpath"),
            rs.getString("reviewContent")
    );

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    @Autowired
    public ReviewDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        reviewJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviews").usingGeneratedKeyColumns("reviewid");
        reviewLikesJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reviewslikes");

    }

    @Override
    public Optional<Review> getReviewById(int currentUserId, int reviewId) {
        StringBuilder sql = new StringBuilder("SELECT r.*,media.name,media.posterpath,users.username, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" (SELECT COUNT(*) FROM reviewsLikes rl WHERE r.reviewid = rl.reviewid) AS reviewLikes, ");
        sql.append(" (SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewsLikes rl2 WHERE r.reviewId = rl2.reviewId AND rl2.userid = ?)  ");
        sql.append(" THEN true ELSE false END) AS hasLiked ");
        args.add(currentUserId);

        sql.append(" FROM reviews r INNER JOIN users ON users.userid = r.userid INNER JOIN media  ");
        sql.append(" ON media.mediaId = r.mediaId WHERE r.reviewId = ? ;");
        args.add(reviewId);

        return jdbcTemplate.query(sql.toString(), args.toArray(), REVIEW_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Review> getReviewsByMediaId(int currentUserId, int mediaId, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT r.*,media.name,media.posterpath,users.username, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" (SELECT COUNT(*) FROM reviewsLikes rl WHERE r.reviewid = rl.reviewid) AS reviewLikes, ");
        sql.append(" (SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewsLikes rl2 WHERE r.reviewId = rl2.reviewId AND rl2.userid = ?)  ");
        sql.append(" THEN true ELSE false END) AS hasLiked ");
        args.add(currentUserId);

        sql.append(" FROM reviews r INNER JOIN users ON users.userid = r.userid INNER JOIN media  ");
        sql.append(" ON media.mediaId = r.mediaId WHERE r.mediaId = ? ");
        sql.append(" LIMIT ? OFFSET ?; ");
        args.add(mediaId);
        args.add(size);
        args.add(pageNumber * size);

        return jdbcTemplate.query(sql.toString(), args.toArray(), REVIEW_ROW_MAPPER);
    }

    public int getReviewsByMediaIdCount(int mediaId){
        return jdbcTemplate.query("SELECT COUNT(*) FROM reviews WHERE mediaId = ?",new Object[]{mediaId},COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }


    @Override
    public List<Review> getMovieReviewsFromUser(int currentUserId, int userId, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT r.*,media.name,media.posterpath,users.username,");
        ArrayList<Object> args = new ArrayList<>();

        sql.append(" (SELECT COUNT(*) FROM reviewsLikes rl WHERE r.reviewid = rl.reviewid) AS reviewLikes, ");
        sql.append(" (SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewsLikes rl2 WHERE r.reviewId = rl2.reviewId AND rl2.userid = ?)  ");
        sql.append(" THEN true ELSE false END) AS hasLiked ");
        args.add(currentUserId);

        sql.append(" FROM reviews r INNER JOIN users ON users.userid = r.userid INNER JOIN media  ");
        sql.append(" ON media.mediaId = r.mediaId WHERE r.userId = ? ");
        sql.append(" LIMIT ? OFFSET ? ");
        args.add(userId);
        args.add(size);
        args.add(pageNumber*size);

        return jdbcTemplate.query(sql.toString(), args.toArray(), REVIEW_ROW_MAPPER);
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

        args.put("reviewContent", reviewContent);
        try{
            reviewJdbcInsert.executeAndReturnKey(args);
        } catch(DuplicateKeyException e){
            throw new UnableToInsertIntoDatabase("Create review failed, already have a review in this movie");
        }

    }

    @Override
    public void deleteReview(int reviewId) {
        String sqlDel = "DELETE FROM reviews WHERE reviewId = ? " ;
        jdbcTemplate.execute(sqlDel, reviewId);
    }

    @Override
    public void likeReview(int userId, int reviewId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("userId", userId);
        args.put("reviewId", reviewId);
        try{
            reviewLikesJdbcInsert.execute(args);
        } catch (Exception e){
            throw new LikingFailedException();
        }
    }


    @Override
    public void removeLikeReview(int userId, int reviewId) {
        StringBuilder sqlDel = new StringBuilder( "DELETE FROM reviewsLikes WHERE reviewId = ? ");
        sqlDel.append(" AND userId =  ?;");
        jdbcTemplate.update(sqlDel.toString(),reviewId,userId);
    }
}