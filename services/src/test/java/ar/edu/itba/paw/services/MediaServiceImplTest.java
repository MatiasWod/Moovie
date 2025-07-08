package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.persistence.MediaDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class MediaServiceImplTest {

    private static final int MEDIA_ID = 42;
    private static final int USER_ID = 7;

    @InjectMocks
    private MediaServiceImpl mediaService;

    @Mock
    private MediaDao mediaDao;

    @Mock
    private UserService userService;

    private Media mockMedia;

    @Before
    public void setUp() {
        mockMedia = mock(Media.class);
    }

    @Test
    public void testGetMediaByIdSuccess() {
        when(mediaDao.getMediaById(MEDIA_ID)).thenReturn(Optional.of(mockMedia));
        when(userService.tryToGetCurrentUserId()).thenReturn(USER_ID);
        when(mediaDao.getWatchlistStatus(MEDIA_ID, USER_ID)).thenReturn(true);
        when(mediaDao.getWatchedStatus(MEDIA_ID, USER_ID)).thenReturn(false);

        Media media = mediaService.getMediaById(MEDIA_ID);

        Assert.assertNotNull(media);
        verify(mediaDao).getMediaById(eq(MEDIA_ID));
        verify(mediaDao).getWatchlistStatus(MEDIA_ID, USER_ID);
        verify(mediaDao).getWatchedStatus(MEDIA_ID, USER_ID);
        verify(mockMedia).setWatchlist(true);
        verify(mockMedia).setWatched(false);
    }

    @Test(expected = MediaNotFoundException.class)
    public void testGetMediaByIdNotFound() {
        when(mediaDao.getMediaById(MEDIA_ID)).thenReturn(Optional.empty());

        mediaService.getMediaById(MEDIA_ID);
    }

    @Test
    public void testGetMediaInMoovieListSuccess() {
        when(mediaDao.getMediaInMoovieList(eq(MEDIA_ID), eq(10), eq(1)))
                .thenReturn(java.util.Arrays.asList(mockMedia));


        List<Media> medias = mediaService.getMediaInMoovieList(MEDIA_ID, 10, 1);

        Assert.assertNotNull(medias);
        Assert.assertEquals(1, medias.size());
        verify(mediaDao).getMediaInMoovieList(MEDIA_ID, 10, 1);
    }

    @Test
    public void testGetWatchlistStatus() {
        when(mediaDao.getWatchlistStatus(MEDIA_ID, USER_ID)).thenReturn(true);

        boolean watchlist = mediaService.getWatchlistStatus(MEDIA_ID, USER_ID);

        Assert.assertTrue(watchlist);
        verify(mediaDao).getWatchlistStatus(MEDIA_ID, USER_ID);
    }
}
