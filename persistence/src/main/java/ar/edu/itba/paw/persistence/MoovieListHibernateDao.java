package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public MoovieListCard getMoovieListCardById(int moovieListId, int currentUserId) {

        MoovieListCardEntity mlcE = Optional.ofNullable(em.find(MoovieListCardEntity.class, moovieListId)).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found") );

        String sqlQuery = "SELECT " +
                " (SELECT COUNT(*) FROM moovielistsContent mlc " +
                " INNER JOIN moovielistscontent mlc2 ON mlc.mediaid = mlc2.mediaid " +
                " JOIN moovieLists ml ON mlc2.moovieListId = ml.moovieListId " +
                " WHERE mlc.moovieListId = :moovieListId AND ml.name = 'Watched' AND ml.userId = :userId) as currentUserWatchAmount, " +
                " (CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = :moovieListId AND mll.userId = :userId) THEN true ELSE false END ) as currentUserHasLiked, " +
                " (CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlf WHERE mlf.moovieListId = :moovieListId AND mlf.userId = :userId) THEN true ELSE false END ) as currentUserHasFollowed";

        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object[] obj = (Object[]) query.getSingleResult();

        MoovieListCardUserStatus userStatus = new MoovieListCardUserStatus(((Number)obj[0]).intValue(),(boolean)obj[1],(boolean)obj[2]);



        return new MoovieListCard(mlcE, userStatus );
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
        // Asumiendo que orderBy y sortOrder son seguros y validados para evitar inyección SQL

//        (CASE WHEN EXISTS ( SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid
//        WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = ? ) THEN true ELSE false END) AS isWatched,

//        String jpql = "SELECT new ar.edu.itba.paw.models.MoovieList.MoovieListContent(" +
//                "mlc, " +
//                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
//                "FROM MoovieList wl INNER JOIN MoovieListContentEntity mlc2 ON wl.moovieListId = mlc2.moovieListId " +
//                "WHERE mlc.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid)) " +
//                "FROM MoovieListContentEntity mlc " +
//                "WHERE mlc.moovieListId = :moovieListId " +
//                "ORDER BY mlc." + orderBy + " " + sortOrder;

        String jpql = "SELECT new ar.edu.itba.paw.models.MoovieList.MoovieListContent(mlc, true) " +
                "FROM MoovieListContentEntity mlc "+
                "WHERE mlc.moovieListId = :moovieListId ";

        TypedQuery<MoovieListContent> query = em.createQuery(jpql, MoovieListContent.class);
        query.setParameter("moovieListId", moovieListId);
//        query.setParameter("userid", userid);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);

        return query.getResultList();
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
