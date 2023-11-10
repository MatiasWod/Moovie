package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.User;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import org.springframework.core.io.Resource;
import javax.persistence.*;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SimpleTimeZone;
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


        MoovieListCard mlc = Optional.ofNullable(em.find(MoovieListCard.class, moovieListId)).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found") );

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

        //Fill the current user status
        mlc.setUserStatus(((Number)obj[0]).intValue(), (boolean)obj[1], (boolean)obj[2]);

        return mlc;
    }

    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc JOIN MoovieListLikes mll " +
                "ON mlc.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .getResultList();

        List<MoovieListCard> cards = new ArrayList<>();

        //Fill the current user status
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];

            mlc.setUserStatus(((Long) result[1]).intValue(),(boolean) result[2], (boolean) result[3] );

            cards.add(mlc);
        }

        return cards;

    }


    @Override
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc JOIN MoovieListFollowers mll " +
                "ON mlc.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .setFirstResult(size*pageNumber).setMaxResults(size).getResultList();

        //Set the current user status
        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
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
        String jpql = "SELECT COUNT(mlc) " +
                "FROM MoovieListCard mlc JOIN MoovieListFollowers mlf " +
                "ON mlc.moovieListId = mlf.moovieList.moovieListId " +
                "WHERE mlf.user.userId = :userId AND type = :type";

        Number result = (Number) em.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("type", type)
                .getSingleResult();

        return result.intValue();
    }

    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId) {

        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " +
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id), " +
                "COUNT(l) AS totallikes" +
                " FROM MoovieListCard mlc LEFT JOIN User u ON mlc.userId = u.userId LEFT JOIN MoovieListLikes l ON mlc.moovieListId = l.moovieList.moovieListId " +
                " LEFT JOIN MoovieListFollowers mlf ON mlf.moovieList.moovieListId = mlc.moovieListId " +
                " WHERE type = 1  AND l.user.userId IN (SELECT user.userId FROM MoovieListLikes WHERE moovieList.moovieListId = :moovieListId) AND mlc.moovieListId <> :moovieListId " +
                " GROUP BY mlc.moovieListId, u.userId ORDER BY totallikes ";


        List<Object[]> results = em.createQuery(jpql)
                .setParameter("moovieListId", moovieListId).setParameter("currentUserId",currentUserId)
                .setFirstResult(pageNumber * size).setMaxResults(size).getResultList();

        //Set the current user status
        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
        }

        return cards;
    }


    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, String orderBy, String order, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieListId = ml.moovieListId WHERE mlc2.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc " +
                "WHERE mlc.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();
        boolean orderFlag = order != null && !order.isEmpty();

        if (searchFlag) {
            jpql += "AND mlc.name LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND mlc.username = :ownerUsername ";
        }

        if(orderFlag){
            jpql += "ORDER BY " + orderBy + " " + order;
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

        List<Object[]> results = query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();

        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
        }

        return cards;
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername, int type, int size, int pageNumber) {
        String jpql = "SELECT COUNT(mlc) " +
                "FROM MoovieListCard mlc "
                +"WHERE mlc.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();

        if (searchFlag) {
            jpql += "AND mlc.name LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND mlc.username = :ownerUsername ";
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

        String jpql = "SELECT new ar.edu.itba.paw.models.MoovieList.MoovieListContent(" +
                "mlc, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieListId " +
                "WHERE mlc.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid)) " +
                "FROM MoovieListContent mlc " +
                "WHERE mlc.moovieListId = :moovieListId " +
                "ORDER BY mlc." + orderBy + " " + sortOrder;


        TypedQuery<MoovieListContent> query = em.createQuery(jpql, MoovieListContent.class);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userid", userid);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);
        return query.getResultList();
    }


    @Override
    public List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder firstQuery = new StringBuilder("FROM Media m ");

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            firstQuery.append(" WHERE m.type = :type ");
        }

        firstQuery.append(" ORDER BY m.").append(featuredListOrder).append(" DESC");

        TypedQuery<Media> q1 = em.createQuery(firstQuery.toString(), Media.class);

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            q1 = q1.setParameter("type", mediaType==1 );
        }


        List<Media> medias = q1.setFirstResult(size*pageNumber)
                .setMaxResults(size).getResultList();

        List<MoovieListContent> toRet = new ArrayList<>();

        for (Media med : medias) {
            toRet.add(new MoovieListContent(med, -1, -1,false));
        }
        return toRet;
    }




    @Override
    public int countWatchedFeaturedMoovieListContent(int moovieListId, int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM ( SELECT * FROM media ");
        query.append(" WHERE mediaid IN(SELECT mediaid FROM media m2 ");

        if(mediaType!=MediaTypes.TYPE_ALL.getType()){
            query.append("WHERE type = :type ");
        }

        query.append("ORDER BY m2.tmdbrating DESC LIMIT 100) AND ");
        query.append(" mediaid IN (SELECT mediaid FROM moovielists ml LEFT JOIN moovielistscontent mlc ON ml.moovielistid = mlc.moovielistid ");
        query.append(" WHERE ml.name = 'Watched' AND userid = :userid) ) AS totalWatched");


        Query q1 = em.createNativeQuery(query.toString()).setParameter("userid", userid);

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            q1 = q1.setParameter("type", mediaType==1 );
        }

        Number toReturn = (Number) q1.getSingleResult();

        return toReturn.intValue();
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
            Integer maxCustomOrder = (Integer) em.createQuery("SELECT MAX(mlc.customOrder) FROM MoovieListContent mlc WHERE mlc.moovieListId = :moovieListId")
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
        MoovieListContent toRemove = em.createQuery("SELECT mlc FROM MoovieListContent mlc WHERE mlc.moovieListId = :moovieListId AND mlc.mediaId = :mediaId", MoovieListContent.class).setParameter("moovieListId", moovieListId).setParameter("mediaId",mediaId).getSingleResult();
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

    @Value("classpath:functions.sql")
    private Resource functions;
    public void executeFunctionScript() {
        try {
            // Read the SQL script from the functions.sql file
            byte[] scriptBytes = FileCopyUtils.copyToByteArray(functions.getInputStream());
            String scriptContent = new String(scriptBytes);

            // Execute the script using JdbcTemplate
            em.createNativeQuery(scriptContent).executeUpdate();



        } catch (Exception e) {
            String lets = e.getMessage();
            // Handle any exceptions
        }
    }

    @Override
    public void updateMoovieListOrder(int moovieListId, int currentPageNumber, int[] toPrevPage, int[] currentPage, int[] toNextPage) {
        executeFunctionScript();
        // la ejecucion/creacion de la function en base de datos parece funcionar, pero no puedo hacer la ejecucion en Hibernate
//        StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("updatecustomOrder");
//        storedProcedure.registerStoredProcedureParameter("mlid", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("firstPosition", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("pageSize", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("toPrev", Integer[].class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("currentPage", Integer[].class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("toNext", Integer[].class, ParameterMode.IN);
//
//        storedProcedure.setParameter("mlid", moovieListId);
//        storedProcedure.setParameter("firstPosition", currentPageNumber * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize() + 1);
//        storedProcedure.setParameter("pageSize", PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
//        storedProcedure.setParameter("toPrev", toPrevPage);
//        storedProcedure.setParameter("currentPage", currentPage);
//        storedProcedure.setParameter("toNext", toNextPage);
//
//        storedProcedure.execute();


//          el principal problema es que Hibernate manda los parametros como 'bytea' segun lee la consola de la ejecucion SQL
//        pero no se puede hacer un CAST de bytea -> integer[] en SQL. Por lo tanto habria que poder setParameter ya casteado al tipo correcto
//        pero en Java ya son int[] . asi que ni idea ¯\_(ツ)_/¯
        em.createNativeQuery("CALL updatecustomorder(:mlid, :firstPos, :size, CAST(:prev AS integer[]), CAST(:current AS integer[]), CAST(:next AS integer[]))")
                .setParameter("mlid", moovieListId)
                .setParameter("firstPos", currentPageNumber * PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize() + 1 )
                .setParameter("size", PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize() )
                .setParameter("prev", toPrevPage)
                .setParameter("current", currentPage)
                .setParameter("next", toNextPage)
                .executeUpdate();
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
