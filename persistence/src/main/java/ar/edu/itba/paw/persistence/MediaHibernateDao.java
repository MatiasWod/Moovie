package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class MediaHibernateDao implements MediaDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, String orderBy, String sortOrder, int size, int pageNumber) {
        return null;
    }

    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return null;
    }

    @Override
    public Optional<Media> getMediaById(int mediaId) {
        return Optional.ofNullable(em.find(Media.class, mediaId));
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return Optional.empty();
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return Optional.empty();
    }

    @Override
    public int getMediaCount(int type, String search, String participantSearch, List<String> genres, List<String> providers) {
        return 0;
    }

    //Following functions needed in order to be safe of sql injection
    private boolean isOrderValid( String order) {
        if(order==null || order.isEmpty()){
            return false;
        }
        order = order.replaceAll(" ","");
        String[] validOrders = {"name", "tmdbrating", "releasedate", "type", "totalrating"};
        for (String element : validOrders) {
            if (element.toLowerCase().equals(order)) {
                return true;
            }
        }
        return false;
    }
    private boolean isSortOrderValid(String so){
        if(so==null || so.isEmpty()){
            return false;
        }
        so = so.replaceAll(" ","");
        if(so.toLowerCase().equals("asc") || so.toLowerCase().equals("desc")){
            return true;
        }
        return false;
    }
}
