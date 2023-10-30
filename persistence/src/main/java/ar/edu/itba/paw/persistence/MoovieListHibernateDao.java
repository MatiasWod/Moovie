package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.User.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class MoovieListHibernateDao implements MoovieListDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return Optional.ofNullable(em.find(MoovieList.class, moovieListId));
    }

    @Override
    public Optional<MoovieListCard> getMoovieListCardById(int moovieListId, int currentUserId) {
        return Optional.ofNullable(em.find(MoovieListCard.class, moovieListId));
    }

    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        return null;
    }

    @Override
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        return null;
    }

    @Override
    public List<User> getMoovieListFollowers(int moovieListId) {
        return null;
    }

    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        return 0;
    }

    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId) {
        return null;
    }

    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, String orderBy, String order, int size, int pageNumber, int currentUserId) {
        return null;
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername, int type, int size, int pageNumber) {
        return 0;
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, int userid, String orderBy, String sortOrder, int size, int pageNumber) {
        return null;
    }

    @Override
    public List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        return null;
    }

    @Override
    public int countWatchedFeaturedMoovieListContent(int moovieListId, int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        return 0;
    }

    @Override
    public MoovieList createMoovieList(int userId, String name, int type, String description) {
        return null;
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        return null;
    }

    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId) {

    }

    @Override
    public void deleteMoovieList(int moovieListId) {

    }

    @Override
    public void updateMoovieListOrder(int moovieListId, int currentPageNumber, int[] toPrevPage, int[] currentPage, int[] toNextPage) {

    }

    @Override
    public void removeLikeMoovieList(int userId, int moovieListId) {

    }

    @Override
    public void likeMoovieList(int userId, int moovieListId) {

    }

    @Override
    public void removeFollowMoovieList(int userId, int moovieListId) {

    }

    @Override
    public void followMoovieList(int userId, int moovieListId) {

    }
}
