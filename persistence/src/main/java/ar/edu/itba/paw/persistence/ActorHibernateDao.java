package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Primary
@Repository
public class ActorHibernateDao implements ActorDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Actor> getAllActorsForMedia(int mediaId) {
        return em.createQuery("SELECT new Actor( a , (SELECT ma.characterName FROM MediaActors ma WHERE ma.media.mediaId = :mediaId AND ma.actor.actorId = a.actorId ) ) " +
                        " FROM MediaActors ma LEFT JOIN ma.actor a WHERE ma.media.mediaId = :mediaId", Actor.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }

    @Override
    public List<Actor> getAllActorsForMedia(int mediaId, int pageNumber, int pageSize) {
        String sql = "SELECT new Actor( a , (SELECT ma.characterName FROM MediaActors ma WHERE ma.media.mediaId = :mediaId AND ma.actor.actorId = a.actorId ) ) " +
                " FROM MediaActors ma LEFT JOIN ma.actor a WHERE ma.media.mediaId = :mediaId";

        int offset = (pageNumber - 1) * pageSize;

        return em.createQuery(sql, Actor.class)
                .setParameter("mediaId", mediaId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int getAllActorsForMediaCount(int mediaId) {
        String sql = "SELECT COUNT(*) " +
                " FROM MediaActors ma LEFT JOIN ma.actor a WHERE ma.media.mediaId = :mediaId";

        return ((Number)em.createQuery(sql)
                .setParameter("mediaId", mediaId)
                .getSingleResult()).intValue();
    }

    @Override
    public Optional<Actor> getActorById(int actorId) {
        final TypedQuery<Actor> query = em.createQuery("FROM Actor WHERE actorId = :actorId", Actor.class).setParameter("actorId", actorId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public int getActorsForQueryCount(String query) {
        String sql = "SELECT COUNT(*) FROM Actor a WHERE LOWER(a.actorName) LIKE :query";

        Number result = (Number) em.createQuery(sql)
                .setParameter("query", "%"+query.toLowerCase()+"%").getSingleResult();

        return result.intValue();
    }

    @Override
    public List<Actor> getActorsForQuery(String query) {
        String sql = "SELECT a FROM Actor a WHERE LOWER(a.actorName) LIKE :query ORDER BY a.actorName ASC";

        TypedQuery<Actor> q = em.createQuery(sql, Actor.class)
                .setParameter("query", "%"+query.toLowerCase()+"%");

        return q.getResultList();
    }

    @Override
    public List<Actor> getActorsForQuery(String query, int pageNumber, int pageSize) {
        String sql = "SELECT a FROM Actor a WHERE LOWER(a.actorName) LIKE :query ORDER BY a.actorName ASC";

        TypedQuery<Actor> q = em.createQuery(sql, Actor.class)
                .setParameter("query", "%"+query.toLowerCase()+"%");

        int offset = (pageNumber - 1) * pageSize;

        return q.setFirstResult(offset).setMaxResults(pageSize).getResultList();
    }

    @Override
    public List<Media> getMediaForActor(int actorId, int pageNumber, int pageSize, int currentUser) {
        String sql = "SELECT new ar.edu.itba.paw.models.Media.Media(m,"+
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE m.mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid) ) " +
                "FROM Media m JOIN MediaActors ma ON m.mediaId = ma.media.mediaId WHERE ma.actor.actorId = :actorId" +
                " ORDER BY m.tmdbRating DESC";

        int offset = (pageNumber - 1) * pageSize;

        TypedQuery<Media> q = em.createQuery(sql, Media.class)
                .setParameter("actorId", actorId)
                .setParameter("userid", currentUser)
                .setFirstResult(offset)
                .setMaxResults(pageSize);

        return q.getResultList();
    }

    @Override
    public int getMediaForActorCount(int actorId) {
        String sql = "SELECT COUNT(*) " +
                "FROM Media m JOIN MediaActors ma ON m.mediaId = ma.media.mediaId WHERE ma.actor.actorId = :actorId";


        TypedQuery<Number> q = em.createQuery(sql, Number.class)
                .setParameter("actorId", actorId);

        return q.getSingleResult().intValue();
    }

}
