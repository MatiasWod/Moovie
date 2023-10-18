import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.persistence.ReviewDaoJdbcImpl;
import config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewDaoJdbcImplTest {

    private static final int VALUE_NOT_LOGGED = -1;

    private static final int EXISTING_USER_ID = 1;

    private static final int EXISTING_REVIEW_ID = 1;
    private static final int EXISTING_REVIEW_MEDIA_ID = 1;
    private static final int EXISTING_MEDIA_ID = 1;

    private static final int SECOND_EXISTING_USER_ID = 2;
    private static final int SECOND_EXISTING_MEDIA_ID = 2;
    private static final int SECOND_EXISTING_REVIEW_ID = 2;

    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewDaoJdbcImpl reviewDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateReview(){
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviews",String.format("userId = %d AND mediaid = %d",
                SECOND_EXISTING_USER_ID,SECOND_EXISTING_MEDIA_ID)));
        reviewDao.createReview(SECOND_EXISTING_USER_ID,SECOND_EXISTING_MEDIA_ID,5,"Amazing movie.");
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviews",
                String.format("userId = %d AND mediaid = %d",SECOND_EXISTING_USER_ID,SECOND_EXISTING_MEDIA_ID)));
    }

    @Test
    public void testGetReview(){
        Optional<Review> review = reviewDao.getReviewById(EXISTING_USER_ID, EXISTING_REVIEW_ID);
        assertTrue(review.isPresent());
        assertEquals(EXISTING_REVIEW_MEDIA_ID,review.get().getReviewId());
    }

    @Test
    public void testGetReviewById(){
        List<Review> review = reviewDao.getReviewsByMediaId(EXISTING_USER_ID,EXISTING_MEDIA_ID,10,0);
        assertEquals(review.size(),2);
        assertEquals(EXISTING_REVIEW_ID,review.get(0).getReviewId());
    }

    @Test
    public void testGetReviewsByUser(){
        List<Review> reviewList = reviewDao.getMovieReviewsFromUser(VALUE_NOT_LOGGED, EXISTING_USER_ID, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), 0);
        assertEquals(reviewList.size(), 2);
        assertEquals(reviewList.get(0).getMediaId(), EXISTING_REVIEW_MEDIA_ID);
    }

    @Test
    public void testLikeReview(){
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviewslikes",String.format("reviewId = %d AND userid = %d",EXISTING_REVIEW_ID,EXISTING_USER_ID)));
        reviewDao.likeReview(EXISTING_USER_ID,EXISTING_REVIEW_ID);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviews",
                String.format("reviewId = %d AND userid = %d",EXISTING_REVIEW_ID,EXISTING_USER_ID)));
    }

    @Test
    public void testDeleteReview(){
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviews",String.format("reviewId = %d",SECOND_EXISTING_REVIEW_ID)));
        reviewDao.deleteReview(SECOND_EXISTING_REVIEW_ID);
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"reviews",String.format("reviewId = %d",SECOND_EXISTING_REVIEW_ID)));
    }

}
