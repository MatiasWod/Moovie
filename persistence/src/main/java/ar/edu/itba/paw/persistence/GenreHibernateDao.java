package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Primary
@Repository
public class GenreHibernateDao implements GenreDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<String> getAllGenres() {
        return em.createQuery("SELECT g.genre FROM Genre g ORDER BY g.genre", String.class)
                .getResultList();
    }

    @Override
    public List<String> getGenresForMedia(int mediaId) {
        return em.createQuery(
                        "SELECT g.genre FROM Genre g JOIN g.medias m WHERE m.id = :mediaId", String.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }

}
