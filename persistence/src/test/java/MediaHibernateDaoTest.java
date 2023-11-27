import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.Media.Movie;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MediaHibernateDaoTest {

    @Autowired
    private MediaHibernateDao mediaHibernateDao;

    private static final int ID_FOR_MEDIA = 1;
    private static final String MEDIA_NAME = "Meg 2: The Trench";
    private static final int INVALID_USER_ID = -1;


    private static final int DIRECTOR_ID = 2127;
    private static final int DIRECTOR_MOVIE_ID1 = 88;
    private static final int DIRECTOR_MOVIE_ID2 = 92;

    @Rollback
    @Test
    public void testGetMediaById(){
        Optional<Media> media = mediaHibernateDao.getMediaById(ID_FOR_MEDIA);

        Assert.assertTrue(media.isPresent());
        Assert.assertEquals(ID_FOR_MEDIA,media.get().getMediaId());
    }

    @Rollback
    @Test
    public void testGetMedia(){
        List<Media> mediaList = mediaHibernateDao.getMedia(MediaTypes.TYPE_MOVIE.getType(), MEDIA_NAME, null, null, null, null, null, null, null, 1, 0, INVALID_USER_ID);

        Assert.assertEquals(ID_FOR_MEDIA, mediaList.get(0).getMediaId());
    }

    @Rollback
    @Test
    public void testGetMediaFailure(){
        List<Media> mediaList = mediaHibernateDao.getMedia(MediaTypes.TYPE_TVSERIE.getType(), MEDIA_NAME, null, null, null, null, null, null, null, 1, 0, INVALID_USER_ID);

        Assert.assertEquals(mediaList.size(), 0);
    }

    @Rollback
    @Test
    public void testGetMediaForDirectorId(){
        List<Movie> mediaList = mediaHibernateDao.getMediaForDirectorId(DIRECTOR_ID, INVALID_USER_ID);

        Assert.assertEquals(2, mediaList.size());
        
        Assert.assertTrue((mediaList.get(0).getMediaId() == DIRECTOR_MOVIE_ID1 && mediaList.get(1).getMediaId() == DIRECTOR_MOVIE_ID2)
                            || (mediaList.get(1).getMediaId() == DIRECTOR_MOVIE_ID1 && mediaList.get(0).getMediaId() == DIRECTOR_MOVIE_ID2) );
    }

    @Rollback
    @Test
    public void testGetMediaCount(){
        List<String> genre = new ArrayList<>();
        genre.add("Adventure");
        int mediacount = mediaHibernateDao.getMediaCount(MediaTypes.TYPE_ALL.getType(), null, null, genre , null, null, null);

        Assert.assertEquals(mediacount,49);
    }



}
