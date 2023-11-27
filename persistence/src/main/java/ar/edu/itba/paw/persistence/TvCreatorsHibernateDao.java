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
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return em.createQuery("SELECT c FROM TVCreators c JOIN c.medias m WHERE m.mediaId = :mediaId", TVCreators.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }


    @Override
    public Optional<TVCreators> getTvCreatorById(int creatorId) {
        final TypedQuery<TVCreators> query = em.createQuery("FROM TVCreators WHERE creatorId = :creatorId ", TVCreators.class).setParameter("creatorId", creatorId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int size) {
        String sql = "SELECT c FROM TVCreators c WHERE LOWER(c.creatorName) LIKE :query ORDER BY c.medias.size DESC";


        TypedQuery<TVCreators> q = em.createQuery(sql, TVCreators.class)
                .setParameter("query","%"+query+"%").setMaxResults(size)
                ;

        List<TVCreators> toReturn = q.getResultList();

        return toReturn;
    }
}
