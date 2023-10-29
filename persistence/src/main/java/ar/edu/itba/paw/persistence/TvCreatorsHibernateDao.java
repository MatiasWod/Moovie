package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class TvCreatorsHibernateDao implements TVCreatorsDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return em.createQuery("FROM TVCreators tv WHERE tv.mediaId = :mediaId", TVCreators.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }
}
