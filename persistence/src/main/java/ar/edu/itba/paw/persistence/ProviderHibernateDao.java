package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Primary
@Repository
public class ProviderHibernateDao implements ProviderDao{

    @PersistenceContext
    private EntityManager em;

    // SELECT p FROM providers p GROUP BY p.providerId, p.providerName, p.logoPath ORDER BY COUNT(*) DESC

    @Override
    public List<Provider> getAllProviders(int pageNumber,int pageSize) {
        return em.createQuery(
                        "SELECT p FROM Provider p GROUP BY p.providerId, p.providerName, p.logoPath ORDER BY p.providerName ASC", Provider.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int getAllProvidersCount() {
        Long count = em.createQuery("SELECT COUNT(DISTINCT p.providerId) FROM Provider p", Long.class).getSingleResult();
        return count.intValue();
    }

    @Override
    public List<Provider> getProvidersForMedia(final int mediaId,int pageNumber,int pageSize) {
        return em.createQuery(
                        "SELECT p FROM Provider p JOIN p.medias m WHERE m.mediaId = :mediaId GROUP BY p.providerId, p.providerName, p.logoPath ORDER BY p.providerName ASC", Provider.class)
                .setParameter("mediaId", mediaId)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int getProvidersForMediaCount(final int mediaId) {
        Long count = em.createQuery(
                        "SELECT COUNT(DISTINCT p.providerId) FROM Provider p JOIN p.medias m WHERE m.mediaId = :mediaId", Long.class)
                .setParameter("mediaId", mediaId)
                .getSingleResult();
        return count.intValue();
    }

    @Override
    public Provider getProviderById(int providerId) {
        List<Provider> results = em.createQuery(
                        "SELECT p FROM Provider p WHERE p.providerId = :providerId", Provider.class
                )
                .setParameter("providerId", providerId)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }
}
