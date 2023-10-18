import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ReviewDaoJdbcImpl;
import config.TestConfig;
import javafx.scene.control.Pagination;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewDaoJdbcImplTest {

    private static final int VALUE_NOT_LOGGED = -1;

    private static final int EXISTING_USER_ID = 1;

    private static final int EXISTING_REVIEW_ID = 1;
    private static final int EXISTING_REVIEW_MEDIA_ID = 1;



    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewDaoJdbcImpl reviewDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }
 /*
    @Test
    public void testCreateReview(){
        reviewDao.createReview();
    }
*/
    @Test
    public void testGetReview(){
        Optional<Review> review = reviewDao.getReviewById(EXISTING_USER_ID, EXISTING_REVIEW_ID);
        assertTrue(review.isPresent());
        assertEquals(EXISTING_REVIEW_MEDIA_ID,review.get().getReviewId());
    }

    @Test
    public void testGetReviewsByUser(){
        List<Review> reviewList = reviewDao.getMovieReviewsFromUser(VALUE_NOT_LOGGED, EXISTING_USER_ID, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), 0);

        assertEquals(reviewList.size(), 2);
        assertEquals(reviewList.get(0).getMediaId(), EXISTING_REVIEW_MEDIA_ID);
    }



}
