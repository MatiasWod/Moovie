import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.persistence.MediaHibernateDao;
import config.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MediaHibernateDaoTest {

    @Autowired
    private MediaHibernateDao mediaHibernateDao;

    private static final int ID_FOR_MEDIA = 1;

    @Rollback
    @Test
    public void testGetMediaById(){
        Optional<Media> media = mediaHibernateDao.getMediaById(ID_FOR_MEDIA);

        Assert.assertTrue(media.isPresent());
        Assert.assertEquals(ID_FOR_MEDIA,media.get().getMediaId());
    }
}
