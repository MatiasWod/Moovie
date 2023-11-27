import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.ReviewHibernateDao;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReviewHibernateDaoTest {
    @Autowired
    private ReviewHibernateDao reviewHibernateDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    private static final int INSERTED_REVIEW_ID = 2;
    private static final int INSERTED_REVIEW_USER_ID = 2;
    private static final int INSERTED_REVIEW_MEDIA_ID = 9;
    private static final int INSERTED_REVIEW_RATING = 5;

    private static final int TO_INSERT_REVIEW_ID = 4;
    private static final int TO_INSERT_REVIEW_USER_ID = 2;
    private static final int TO_INSERT_REVIEW_MEDIA_ID = 4;
    private static final int TO_INSERT_REVIEW_RATING_ID = 4;
    private static final String TO_INSERT_REVIEW_DESCRIPTION = "My description";
    private static final int PAGE_SIZE = 10;
    private static final String REVIEWS_TABLE = "reviews";

    private static final int ROLE_USER=1;

    private User user;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
        user= new User.Builder("testUser","testEmail","testPassword",ROLE_USER,0).userId(TO_INSERT_REVIEW_USER_ID).build();
    }

    @Rollback
    @Test
    public void testGetReviewById(){
        final Optional<Review> review = reviewHibernateDao.getReviewById(INSERTED_REVIEW_USER_ID,INSERTED_REVIEW_ID);

        Assert.assertTrue(review.isPresent());
        Assert.assertEquals(INSERTED_REVIEW_RATING,review.get().getRating());
    }

    @Rollback
    @Test
    public void testGetReviewsByMediaId(){
        final List<Review> review = reviewHibernateDao.getReviewsByMediaId(INSERTED_REVIEW_USER_ID,INSERTED_REVIEW_MEDIA_ID,PAGE_SIZE,0);
        Assert.assertFalse(review.isEmpty());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REVIEWS_TABLE, String.format("mediaid = '%d'", INSERTED_REVIEW_MEDIA_ID)));
    }

    @Rollback
    @Test
    public void testGetReviewsFromUser(){
        final List<Review> review = reviewHibernateDao.getMovieReviewsFromUser(INSERTED_REVIEW_USER_ID,INSERTED_REVIEW_USER_ID,PAGE_SIZE,0);
        Assert.assertFalse(review.isEmpty());
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REVIEWS_TABLE, String.format("userid = '%d'", INSERTED_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testCreateReview(){
        reviewHibernateDao.createReview(user,TO_INSERT_REVIEW_MEDIA_ID,TO_INSERT_REVIEW_RATING_ID,TO_INSERT_REVIEW_DESCRIPTION,ReviewTypes.REVIEW_MEDIA);
        entityManager.flush();
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REVIEWS_TABLE, String.format("userid = '%d'", TO_INSERT_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testDeleteReview(){
        reviewHibernateDao.deleteReview(INSERTED_REVIEW_USER_ID,ReviewTypes.REVIEW_MEDIA);
        entityManager.flush();
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, REVIEWS_TABLE, String.format("userid = '%d'", INSERTED_REVIEW_USER_ID)));
    }

    @Rollback
    @Test
    public void testGetReviewsByMediaIdCount(){
        final int count = reviewHibernateDao.getReviewsByMediaIdCount(INSERTED_REVIEW_MEDIA_ID);
        Assert.assertEquals(1,count);
    }

}
