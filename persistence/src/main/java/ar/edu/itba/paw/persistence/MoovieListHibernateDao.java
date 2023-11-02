package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        String jpql = "SELECT mlcE, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContentEntity mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlcE.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlcE.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlcE.id) " +
                "FROM MoovieListCardEntity mlcE JOIN MoovieListLikes mll " +
                "ON mlcE.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .getResultList();

        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCardEntity mlcE = (MoovieListCardEntity) result[0];
            mlcE.setImages(getImagesForMoovieList(mlcE.getMoovieListId()));
            Long currentUserWatchAmount = (Long) result[1];
            boolean currentUserHasLiked = (boolean) result[2];
            boolean currentUserHasFollowed = (boolean) result[3];

            MoovieListCardUserStatus mlcUS = new MoovieListCardUserStatus(currentUserWatchAmount.intValue(), currentUserHasLiked, currentUserHasFollowed);
            MoovieListCard card = new MoovieListCard(mlcE, mlcUS);
            cards.add(card);
        }

        return cards;

    }

    public List<String> getImagesForMoovieList(int moovieListId) {
        String sql = "SELECT m.posterPath " +
                "FROM moovielistscontent mlc INNER JOIN media m ON mlc.mediaid = m.mediaid " +
                "WHERE mlc.moovieListId = :moovieListId " +
                "ORDER BY mlc.customOrder ASC LIMIT 4";

        List<String> images = em.createNativeQuery(sql)
                .setParameter("moovieListId", moovieListId)
                .getResultList();

        return images;
    }


    @Override
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlcE, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContentEntity mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlcE.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlcE.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlcE.id) " +
                "FROM MoovieListCardEntity mlcE JOIN MoovieListFollowers mll " +
                "ON mlcE.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .getResultList();

        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCardEntity mlcE = (MoovieListCardEntity) result[0];
            mlcE.setImages(getImagesForMoovieList(mlcE.getMoovieListId()));
            Long currentUserWatchAmount = (Long) result[1];
            boolean currentUserHasLiked = (boolean) result[2];
            boolean currentUserHasFollowed = (boolean) result[3];

            MoovieListCardUserStatus mlcUS = new MoovieListCardUserStatus(currentUserWatchAmount.intValue(), currentUserHasLiked, currentUserHasFollowed);
            MoovieListCard card = new MoovieListCard(mlcE, mlcUS);
            cards.add(card);
        }

        return cards;
    }

    @Override
    public List<User> getMoovieListFollowers(int moovieListId) {
        return em.createQuery("SELECT mlf.user " +
                        "FROM MoovieListFollowers mlf " +
                        "WHERE mlf.moovieList.moovieListId = :moovieListId"
                        , User.class)
                .setParameter("moovieListId", moovieListId)
                .getResultList();
    }

    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        return 0;
    }

    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId) {
        // TODO: implementar algoritmo para la recomendacion
        return getLikedMoovieListCards(currentUserId,1,25,0,currentUserId);
    }

    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, String orderBy, String order, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlcE, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContentEntity mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlcE.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlcE.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlcE.id) " +
                "FROM MoovieListCardEntity mlcE "
                +"WHERE mlcE.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();
        boolean orderFlag = order != null && !order.isEmpty();

        if (searchFlag) {
            jpql += "AND mlcE.name LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND mlcE.username = :ownerUsername ";
        }


        Query query = em.createQuery(jpql)
                .setParameter("currentUserId",currentUserId)
                .setParameter("type", type);

        if (searchFlag){
            query.setParameter("search", "%"+search+"%");
        }
        if (usernameFlag){
            query.setParameter("ownerUsername", ownerUsername);
        }

        List<Object[]> results = query.setFirstResult(pageNumber*size).setMaxResults(size)
                .getResultList();

        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCardEntity mlcE = (MoovieListCardEntity) result[0];
            mlcE.setImages(getImagesForMoovieList(mlcE.getMoovieListId()));
            Long currentUserWatchAmount = (Long) result[1];
            boolean currentUserHasLiked = (boolean) result[2];
            boolean currentUserHasFollowed = (boolean) result[3];

            MoovieListCardUserStatus mlcUS = new MoovieListCardUserStatus(currentUserWatchAmount.intValue(), currentUserHasLiked, currentUserHasFollowed);
            MoovieListCard card = new MoovieListCard(mlcE, mlcUS);
            cards.add(card);
        }

        return cards;
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername, int type, int size, int pageNumber) {
        String jpql = "SELECT COUNT(mlcE) " +
                "FROM MoovieListCardEntity mlcE "
                +"WHERE mlcE.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();

        if (searchFlag) {
            jpql += "AND mlcE.name LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND mlcE.username = :ownerUsername ";
        }
        Query query = em.createQuery(jpql)
                .setParameter("type", type);

        if (searchFlag){
            query.setParameter("search", "%"+search+"%");
        }
        if (usernameFlag){
            query.setParameter("ownerUsername", ownerUsername);
        }

        Long toReturn = (Long) query.getSingleResult();

        return toReturn.intValue();
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, int userid, String orderBy, String sortOrder, int size, int pageNumber) {
        // Asumiendo que orderBy y sortOrder son seguros y validados para evitar inyección SQL
        // TODO: hacer un previo check de orderBy y sortOrder
//        List<MoovieListContentEntity> content = em.createQuery("SELECT mlc FROM MoovieListContentEntity mlc"
//                + " WHERE mlc.moovieListId = :moovieListId" +
//                " ORDER BY mlc."+orderBy+" " +sortOrder
//                        , MoovieListContentEntity.class)
//                .setParameter("moovieListId", moovieListId)
//                .setFirstResult(pageNumber * size).setMaxResults(size)
//                .getResultList();
//
//        String sqlQuery = "SELECT (CASE WHEN EXISTS (SELECT 1 FROM moovielists ml INNER JOIN moovieListsContent mlc2 ON ml.moovielistid = mlc2.moovielistid " +
//                "WHERE m.mediaId = mlc2.mediaId AND ml.name = 'Watched' AND ml.userid = :userId) THEN TRUE ELSE FALSE END) " +
//                "FROM media m JOIN moovielistscontent mlc ON m.mediaid = mlc.mediaid " +
//                "WHERE mlc.moovielistid = :moovieListId " +
//                "ORDER BY " + orderBy + " " + sortOrder;
//
//        List<Object> watchedObjects = em.createNativeQuery(sqlQuery)
//                .setParameter("userId", userid).setParameter("moovieListId", moovieListId)
//                .setFirstResult(size * pageNumber).setMaxResults(size)
//                .getResultList();
//
//        List<Boolean> watched = watchedObjects.stream()
//                .map(result -> (Boolean) result)
//                .collect(Collectors.toList());
//
//
//        List<MoovieListContent> toReturn = new ArrayList<>();
//        int i = 0;
//
//        for (MoovieListContentEntity mlc : content){
//            MoovieListContent aux = new MoovieListContent(mlc, watched.get(i++));
//            toReturn.add(aux);
//        }
//
//        return toReturn;

        String jpql = "SELECT new ar.edu.itba.paw.models.MoovieList.MoovieListContent(" +
                "mlc, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContentEntity mlc2 ON wl.moovieListId = mlc2.moovieListId " +
                "WHERE mlc.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid)) " +
                "FROM MoovieListContentEntity mlc " +
                "WHERE mlc.moovieListId = :moovieListId "
//              +  "ORDER BY mlc." + orderBy + " " + sortOrder;
                ;


        TypedQuery<MoovieListContent> query = em.createQuery(jpql, MoovieListContent.class);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userid", userid);
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
        MoovieList newMoovieList = new MoovieList(userId, name, description, type);
        try{
            em.persist(newMoovieList);
            return newMoovieList;
        } catch(DuplicateKeyException e){
            throw new UnableToInsertIntoDatabase("Create MoovieList failed, already have a table with the given name");
        }

    }


    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        MoovieList updatedMoovieList = em.find(MoovieList.class, moovieListid);
        if (updatedMoovieList != null) {
            Integer maxCustomOrder = (Integer) em.createQuery("SELECT MAX(mlcE.customOrder) FROM MoovieListContentEntity mlcE WHERE mlcE.moovieListId = :moovieListId")
                    .setParameter("moovieListId", moovieListid)
                    .getSingleResult();

            if (maxCustomOrder == null) {
                maxCustomOrder = 0;
            }

            for (Integer mediaId : mediaIdList) {
                maxCustomOrder++;
                em.createNativeQuery("INSERT INTO moovielistscontent (mediaid, moovielistid, customorder) VALUES (:mediaId, :moovielistid, :customorder)")
                        .setParameter("mediaId", mediaId)
                        .setParameter("moovielistid", moovieListid)
                        .setParameter("customorder", maxCustomOrder)
                        .executeUpdate();
            }
        }

        return updatedMoovieList;
    }


    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId) {
        MoovieListContentEntity toRemove = em.createQuery("SELECT mlc FROM MoovieListContentEntity mlc WHERE mlc.moovieListId = :moovieListId AND mlc.mediaId = :mediaId", MoovieListContentEntity.class).setParameter("moovieListId", moovieListId).setParameter("mediaId",mediaId).getSingleResult();
        if (toRemove != null){
            em.remove(toRemove);
        }

    }

    @Override
    public void deleteMoovieList(int moovieListId) {
        MoovieList toRemove = em.find(MoovieList.class, moovieListId);
        if (toRemove != null )
            em.remove(toRemove);
    }

    @Override
    public void updateMoovieListOrder(int moovieListId, int currentPageNumber, int[] toPrevPage, int[] currentPage, int[] toNextPage) {

    }

    @Override
    public void removeLikeMoovieList(int userId, int moovieListId) {
        MoovieListLikes like = em.createQuery("SELECT mll" +
                        " FROM MoovieListLikes mll" +
                        " WHERE mll.user.id = :userId AND mll.moovieList.id = :moovieListId"
                        , MoovieListLikes.class)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .getSingleResult();

        // Si la entidad existe, eliminar el like
        if (like != null) {
            em.remove(like);
        }
    }

    @Override
    public void likeMoovieList(int userId, int moovieListId) {

        User user = em.find(User.class, userId);
        MoovieList moovieList = em.find(MoovieList.class, moovieListId);
        MoovieListLikes newLike = new MoovieListLikes(moovieList, user);
        em.persist(newLike);
    }

    @Override
    public void removeFollowMoovieList(int userId, int moovieListId) {
        MoovieListFollowers follow = em.createQuery("SELECT mll" +
                                " FROM MoovieListFollowers mll" +
                                " WHERE mll.user.id = :userId AND mll.moovieList.id = :moovieListId"
                        , MoovieListFollowers.class)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .getSingleResult();

        // Si la entidad existe, eliminar el follow
        if (follow != null) {
            em.remove(follow);
        }
    }

    @Override
    public void followMoovieList(int userId, int moovieListId) {

        User user = em.find(User.class, userId);
        MoovieList moovieList = em.find(MoovieList.class, moovieListId);
        MoovieListFollowers newFollow = new MoovieListFollowers(moovieList, user);
        em.persist(newFollow);
    }
}
