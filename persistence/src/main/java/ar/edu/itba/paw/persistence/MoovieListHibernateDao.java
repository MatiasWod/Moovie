package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.User;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
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
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
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
    public List<Media> getRecommendedMediaToAdd(int moovieListId, int size) {
        String jpql = "SELECT m.mediaID FROM media m " +
                "LEFT JOIN mediagenres g ON g.mediaid = m.mediaid " +
                "WHERE g.genreid = (SELECT sq1.genreid FROM " +
                "(SELECT mlc.moovielistid, mg.genreid, COUNT(mg.genreid) AS countGenres " +
                "FROM moovielistscontent mlc INNER JOIN mediagenres mg ON mg.mediaid = mlc.mediaid " +
                "WHERE mlc.moovielistid = :moovieListId GROUP BY mg.genreid, mlc.moovielistid " +
                "ORDER BY countGenres DESC LIMIT 1) AS sq1) " +
                "AND m.mediaID NOT IN (SELECT mlc2.mediaid FROM moovielistscontent mlc2 WHERE moovielistid = :moovieListId ) " +
                "ORDER BY m.tmdbrating DESC";

        List<Integer> ids = em.createNativeQuery(jpql).setParameter("moovieListId", moovieListId).setFirstResult(0).setMaxResults(size).getResultList();

        TypedQuery<Media> query = em.createQuery("SELECT m FROM Media m WHERE m.mediaId in (:ids)", Media.class).setParameter("ids", ids);

        return query.getResultList();
    }

    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, String orderBy, String order, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
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
    public List<Media> getMoovieListContent(int moovieListId, int userid, String orderBy, String sortOrder, int size, int pageNumber) {

        String jpql = "SELECT new ar.edu.itba.paw.models.Media.Media(" +
                "m, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE mlc.media.mediaId = mlc2.media.mediaId AND wl.name = 'Watched' AND wl.userId = :userid),  " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE mlc.media.mediaId = mlc2.media.mediaId AND wl.name = 'Watchlist' AND wl.userId = :userid))  " +
                "FROM MoovieListContent mlc LEFT JOIN Media m ON mlc.media.mediaId = m.mediaId " +
                "WHERE mlc.moovieList.moovieListId = :moovieListId " +
                "ORDER BY ";

        if(orderBy.equals("customOrder")){
            jpql += " mlc." + orderBy + " " + sortOrder;
        } else{
            jpql += " m." + orderBy + " " + sortOrder;
        }


        TypedQuery<Media> query = em.createQuery(jpql, Media.class);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userid", userid);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);
        return query.getResultList();
    }


    @Override
    public List<Media> getFeaturedMoovieListContent( int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder firstQuery = new StringBuilder("SELECT m.mediaId FROM Media m ");

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            firstQuery.append(" WHERE m.type = :type ");
        }

        firstQuery.append(" ORDER BY m.").append(featuredListOrder).append(" DESC");

        TypedQuery<Integer> q1 = em.createQuery(firstQuery.toString(), Integer.class);

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            q1 = q1.setParameter("type", mediaType==1 );
        }


        List<Integer> medias = q1.setFirstResult(size*pageNumber)
                .setMaxResults(100).getResultList();


        //Two queryes are needed in order to make a LIMIT 25 inside the LIMI 100

        String jpql = "SELECT new ar.edu.itba.paw.models.Media.Media(" +
                "m, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE m.mediaId = mlc2.media.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE m.mediaId = mlc2.media.mediaId AND wl.name = 'Watched' AND wl.userId = :userid))" +
                "FROM Media m " +
                "WHERE m.mediaId IN (:medias) ORDER BY ";

        if((orderBy!=null && sortOrder != null) && (!orderBy.isEmpty() && !sortOrder.isEmpty()) && !orderBy.equals("customOrder") ){
            jpql += " m." + orderBy + " " + sortOrder;
        } else{
            jpql += " m." + featuredListOrder + " " + "DESC";
        }


        TypedQuery<Media> query = em.createQuery(jpql, Media.class);
        query.setParameter("medias", medias).setParameter("userid", userid)
                .setFirstResult(pageNumber * size).setMaxResults(size);
        return query.getResultList();
    }




    @Override
    public int countWatchedFeaturedMoovieListContent(int mediaType, int userid, String featuredListOrder) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM ( ");
        query.append(" SELECT * FROM media WHERE ");

        if(mediaType!=MediaTypes.TYPE_ALL.getType()){
            query.append(" type = :type AND ");
        }

        query.append("  mediaid IN ( SELECT mediaid FROM ( SELECT m2.mediaid, COUNT(r.rating) AS votecount ");
        query.append(" FROM media m2 LEFT JOIN reviews r ON m2.mediaid = r.mediaid GROUP BY m2.mediaid ORDER BY votecount DESC LIMIT 100) sq1 ) ");
        query.append(" AND mediaid IN ( SELECT mlc.mediaid FROM moovielists ml LEFT JOIN moovielistscontent mlc ON ml.moovielistid = mlc.moovielistid ");
        query.append(" WHERE ml.name = 'Watched' AND userid = :userid )) AS sq2; ");

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
            Integer maxCustomOrder = (Integer) em.createQuery("SELECT MAX(mlc.customOrder) FROM MoovieListContent mlc WHERE mlc.moovieList.moovieListId = :moovieListId")
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
        MoovieListContent toRemove = em.createQuery("SELECT mlc FROM MoovieListContent mlc WHERE mlc.moovieList.moovieListId = :moovieListId AND mlc.media.mediaId = :mediaId", MoovieListContent.class).setParameter("moovieListId", moovieListId).setParameter("mediaId",mediaId).getSingleResult();
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

        em.createNativeQuery("SELECT updatecustomorder(:mlid, :firstPos, :size, :prev::integer[], :current::integer[], :next::integer[])")
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
