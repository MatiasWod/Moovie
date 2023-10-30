package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Primary
@Repository
public class ActorHibernateDao implements ActorDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Actor> getAllActorsForMedia(int mediaId) {
        return em.createQuery("SELECT a.mediaId, a.actorId, a.actorName, a.characterName, a.profilePath FROM Actor a WHERE mediaId = :mediaId", Actor.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }
}
