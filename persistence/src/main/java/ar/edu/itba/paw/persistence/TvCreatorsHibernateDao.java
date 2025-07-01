package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class TvCreatorsHibernateDao implements TVCreatorsDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId, int pageNumber, int pageSize) {
        String sql = "SELECT c FROM TVCreators c JOIN c.medias m WHERE m.mediaId = :mediaId";

        int offset = (pageNumber - 1) * pageSize;

        return em.createQuery(sql, TVCreators.class)
                .setParameter("mediaId", mediaId)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int getTvCreatorsByMediaIdCount(int mediaId) {
        return em.createQuery("SELECT COUNT(*) FROM TVCreators c JOIN c.medias m WHERE m.mediaId = :mediaId", Number.class)
                .setParameter("mediaId", mediaId)
                .getSingleResult().intValue();
    }


    @Override
    public Optional<TVCreators> getTvCreatorById(int creatorId) {
        final TypedQuery<TVCreators> query = em.createQuery("FROM TVCreators WHERE creatorId = :creatorId ", TVCreators.class).setParameter("creatorId", creatorId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Media> getMediasForTVCreator(int creatorId, int pageNumber, int pageSize, int currentUser) {
        String sql = "SELECT new ar.edu.itba.paw.models.Media.Media(m,"+
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE m.mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid) ) " +
                "FROM Media m JOIN MediaCreators mc ON m.mediaId = mc.media.mediaId WHERE mc.tvCreators.creatorId = :creatorId" +
                " ORDER BY m.tmdbRating DESC";

        int offset = (pageNumber - 1) * pageSize;

        TypedQuery<Media> q = em.createQuery(sql, Media.class)
                .setParameter("creatorId", creatorId)
                .setParameter("userid", currentUser)
                .setFirstResult(offset)
                .setMaxResults(pageSize);

        return q.getResultList();
    }

    @Override
    public int getMediasForTVCreatorCount(int creatorId) {
        String sql = "SELECT COUNT(*) " +
                "FROM Media m JOIN MediaCreators mc ON m.mediaId = mc.media.mediaId WHERE mc.tvCreators.creatorId = :creatorId";


        Number q = (Number) em.createQuery(sql)
                .setParameter("creatorId", creatorId)
                .getSingleResult();

        return q.intValue();
    }


    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int pageNumber, int pageSize) {
        String sql = "SELECT c FROM TVCreators c WHERE LOWER(c.creatorName) LIKE :query ORDER BY c.medias.size DESC";

        int offset = (pageNumber - 1) * pageSize;

        TypedQuery<TVCreators> q = em.createQuery(sql, TVCreators.class)
                .setParameter("query","%"+query+"%")
                .setFirstResult(offset)
                .setMaxResults(pageSize);

        return q.getResultList();
    }

    @Override
    public int getTVCreatorsForQueryCount(String query) {
        String sql = "SELECT COUNT(*) FROM TVCreators c WHERE LOWER(c.creatorName) LIKE :query";


        TypedQuery<Number> q = em.createQuery(sql, Number.class)
                .setParameter("query","%"+query+"%");

        return q.getSingleResult().intValue();
    }
}
