package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MoovieListServiceImplTest {

    private static final int MOOVIE_LIST_ID = 1;
    private static final int USER_ID = 10;
    private static final int OTHER_USER_ID = 20;
    private static final int MEDIA_ID = 5;
    private static final User USER = new User();
    private static final User OTHER_USER = new User();

    @InjectMocks
    private MoovieListServiceImpl moovieListService;

    @Mock
    private MoovieListDao mockMoovieListDao;

    @Mock
    private UserService mockUserService;

    @Mock
    private EmailService mockEmailService;

    private MoovieList privateListOwned;
    private MoovieList privateListNotOwned;
    private MoovieListCard privateListCardOwned;
    private MoovieListCard privateListCardNotOwned;
    private MoovieListCard publicListCard;

    @Before
    public void setup() {
        // Lista privada propiedad del usuario actual
        privateListOwned = new MoovieList();
        privateListOwned.setUserId(USER_ID);
        privateListOwned.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType());

        // Lista privada NO propiedad del usuario actual
        privateListNotOwned = new MoovieList();
        privateListNotOwned.setUserId(OTHER_USER_ID);
        privateListNotOwned.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType());

        // MoovieListCard privada propiedad del usuario actual
        privateListCardOwned = new MoovieListCard();
        privateListCardOwned.setUser(USER);
        privateListCardOwned.setUserId(USER_ID);
        privateListCardOwned.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType());

        // MoovieListCard privada NO propiedad del usuario actual
        privateListCardNotOwned = new MoovieListCard();
        privateListCardNotOwned.setUser(OTHER_USER);
        privateListCardNotOwned.setUserId(OTHER_USER_ID);
        privateListCardNotOwned.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType());

        // MoovieListCard pública
        publicListCard = new MoovieListCard();
        publicListCard.setUser(OTHER_USER);
        publicListCard.setUserId(OTHER_USER_ID);
        publicListCard.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
        publicListCard.setCurrentUserHasLiked(false);
        publicListCard.setLikeCount(3);

        // Mockeo el id usuario actual
        when(mockUserService.tryToGetCurrentUserId()).thenReturn(USER_ID);
    }

    @Test
    public void testGetMoovieListById_OwnerCanAccessPrivateList() {
        when(mockMoovieListDao.getMoovieListById(MOOVIE_LIST_ID)).thenReturn(Optional.of(privateListOwned));

        MoovieList result = moovieListService.getMoovieListById(MOOVIE_LIST_ID);

        assertEquals(USER_ID, result.getUserId());
    }

    @Test(expected = ForbiddenException.class)
    public void testGetMoovieListById_NotOwnerCannotAccessPrivateList() {
        when(mockMoovieListDao.getMoovieListById(MOOVIE_LIST_ID)).thenReturn(Optional.of(privateListNotOwned));

        moovieListService.getMoovieListById(MOOVIE_LIST_ID);
    }

    @Test(expected = InvalidAccessToResourceException.class)
    public void testGetMoovieListById_NotLoggedUserCannotAccessPrivateList() {
        when(mockMoovieListDao.getMoovieListById(MOOVIE_LIST_ID)).thenReturn(Optional.of(privateListNotOwned));
        when(mockUserService.tryToGetCurrentUserId()).thenThrow(new UserNotLoggedException());

        moovieListService.getMoovieListById(MOOVIE_LIST_ID);
    }

    @Test
    public void testGetMoovieListCardById_OwnerCanAccessPrivateCard() {
        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(privateListCardOwned);

        MoovieListCard card = moovieListService.getMoovieListCardById(MOOVIE_LIST_ID);

        assertEquals(USER_ID, card.getUserId());
    }

    @Test(expected = InvalidAccessToResourceException.class)
    public void testGetMoovieListCardById_NotOwnerCannotAccessPrivateCard() {
        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(privateListCardNotOwned);

        moovieListService.getMoovieListCardById(MOOVIE_LIST_ID);
    }

    @Test
    public void testDeleteMediaFromMoovieList_OwnerDeletesMedia() {
        when(mockMoovieListDao.getMoovieListById(MOOVIE_LIST_ID)).thenReturn(Optional.of(privateListOwned));
        when(mockUserService.tryToGetCurrentUserId()).thenReturn(USER_ID);

        moovieListService.deleteMediaFromMoovieList(MOOVIE_LIST_ID, MEDIA_ID);

        verify(mockMoovieListDao).deleteMediaFromMoovieList(MOOVIE_LIST_ID, MEDIA_ID);
    }


    @Test
    public void testLikeMoovieList_PublicListSuccess() {
        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(publicListCard);

        boolean liked = moovieListService.likeMoovieList(MOOVIE_LIST_ID);

        assertTrue(liked);
        verify(mockMoovieListDao).likeMoovieList(USER_ID, MOOVIE_LIST_ID);
    }

    @Test(expected = ForbiddenException.class)
    public void testLikeMoovieList_PrivateListThrows() {
        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(privateListCardOwned);

        moovieListService.likeMoovieList(MOOVIE_LIST_ID);
    }

    @Test(expected = UserNotLoggedException.class)
    public void testLikeMoovieList_UserNotLogged() {
        when(mockUserService.tryToGetCurrentUserId()).thenReturn(-1);

        moovieListService.likeMoovieList(MOOVIE_LIST_ID);
    }

    @Test
    public void testRemoveLikeMoovieList_UserHasLiked() {
        MoovieListCard likedCard = new MoovieListCard();
        likedCard.setCurrentUserHasLiked(true);
        likedCard.setUser(OTHER_USER);
        likedCard.setUserId(OTHER_USER_ID);
        likedCard.setType(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());

        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(likedCard);

        boolean removed = moovieListService.removeLikeMoovieList(MOOVIE_LIST_ID);

        assertTrue(removed);
        verify(mockMoovieListDao).removeLikeMoovieList(USER_ID, MOOVIE_LIST_ID);
    }

    @Test
    public void testRemoveLikeMoovieList_UserHasNotLiked() {
        MoovieListCard notLikedCard = new MoovieListCard();
        notLikedCard.setCurrentUserHasLiked(false);

        when(mockMoovieListDao.getMoovieListCardById(MOOVIE_LIST_ID, USER_ID)).thenReturn(notLikedCard);

        boolean removed = moovieListService.removeLikeMoovieList(MOOVIE_LIST_ID);

        assertFalse(removed);
        verify(mockMoovieListDao, never()).removeLikeMoovieList(anyInt(), anyInt());
    }
}
