package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
