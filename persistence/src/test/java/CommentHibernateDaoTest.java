import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.persistence.CommentDaoImpl;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CommentHibernateDaoTest {
    @Autowired
    private CommentDaoImpl commentDao;

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    private JdbcTemplate jdbcTemplate;

    private static final int TO_INSERT_COMMENT_REVIEW_ID = 2;
    private static final String TO_INSERT_COMMENT_DESCRIPTION = "My comment =)";
    private static final int TO_INSERT_COMMENT_USER_ID = 4;
    private static final String COMMENTS_TABLE = "comments";

    private static final int INSERTED_COMMENT_REVIEW_ID = 3;
    private static final int INSERTED_COMMENT_USER_ID = 2;
    private static final int PAGE_SIZE = 10;

    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Rollback
    @Test
    public void getCommentsFromReview(){
        List<Comment> commentList = commentDao.getComments(INSERTED_COMMENT_REVIEW_ID,INSERTED_COMMENT_USER_ID,PAGE_SIZE,0);
        Assert.assertFalse(commentList.isEmpty());
        Assert.assertEquals(TO_INSERT_COMMENT_USER_ID,commentList.get(0).getCommentId());
    }

}
