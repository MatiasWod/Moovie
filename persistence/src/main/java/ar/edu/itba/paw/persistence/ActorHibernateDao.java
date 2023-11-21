package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.services.ActorService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public Optional<Actor> getActorById(int actorId) {
        final TypedQuery<Actor> query = em.createQuery("FROM Actor WHERE actorId = :actorId", Actor.class).setParameter("actorId", actorId);
        return Optional.ofNullable(query.getSingleResult());
    }

}
