package ar.edu.itba.paw.persistence;

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
    public List<Provider> getAllProviders() {
        return em.createQuery("SELECT p FROM Provider p JOIN p.medias GROUP BY p.providerId, p.providerName, p.logoPath ORDER BY COUNT(*) DESC", Provider.class)
                .getResultList();
    }
}
