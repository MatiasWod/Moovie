package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
//        final Query nativeQuery=em.createNativeQuery("SELECT * FROM media");
//
//        //TODO fijarese si esto esta bien aca o va al final
//        nativeQuery.setMaxResults(size).setFirstResult((pageNumber-1)*size);
//
//        @SuppressWarnings("unchecked")
//        final List<Media> result = (List<Media>) nativeQuery.getResultList().stream().map(Object::toString).collect(Collectors.toList());
//
//        TypedQuery<Media> query = em.createQuery("from Media where mediaId in :mediaids", Media.class);
//        query.setParameter("mediaids", result);
//
//        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
//        if (type != 0) {
//            TypedQuery<Media> query2 = em.createQuery("from Media where type = :types", Media.class);
//            query.setParameter("type", type);
//        }else{
//            TypedQuery<Media> query2 = em.createQuery("from Media where type IS NOT NULL", Media.class);
//        }
//
//        // Add the genres filter
//        if(genres!=null && !genres.isEmpty()){
//            TypedQuery<Media> query3 = em.createQuery("from Media where genre in :genres", Media.class);
//            query.setParameter("genres", genres);
//        }
//
//        // Add the providers filter
//        if(providers!=null && !providers.isEmpty()){
//            TypedQuery<Media> query4 = em.createQuery("from Media where provider in :providers", Media.class);
//            query.setParameter("providers", providers);
//        }
//
//        //Input the search
//        if(search!=null && !search.isEmpty()){
//            TypedQuery<Media> query5 = em.createQuery("from Media where name like :search", Media.class);
//            query.setParameter("search", search);
//        }
//
//        // Input its participants in actors, media.name, creators and directors
//        if(participant!=null && !participant.isEmpty()){
//            TypedQuery<Media> query6 = em.createQuery("from Media where participant like :participant", Media.class);
//            query.setParameter("participant", participant);
//        }
//
//        // Order by
//        if(isOrderValid(orderBy) && isSortOrderValid(sortOrder)){
//            TypedQuery<Media> query7 = em.createQuery("from Media order by :orderBy :sortOrder", Media.class);
//            query.setParameter("orderBy", orderBy);
//            query.setParameter("sortOrder", sortOrder);
//        }
//
//
//        return query.getResultList();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Media> criteriaQuery = criteriaBuilder.createQuery(Media.class);
        Root<Media> mediaRoot = criteriaQuery.from(Media.class);

        // Create a list to store predicates for filtering
        List<Predicate> predicates = new ArrayList<>();

        // Add type filter
        if (type == 0) {
            predicates.add(criteriaBuilder.equal(mediaRoot.get("type"), 0)); // Assuming '0' represents movies
        } else if (type == 1) {
            predicates.add(criteriaBuilder.equal(mediaRoot.get("type"), 1)); // Assuming '1' represents TV shows
        }

        // Add genres filter
        if (genres != null && !genres.isEmpty()) {
            predicates.add(mediaRoot.get("genre").in(genres));
        }

        // Add providers filter
        if (providers != null && !providers.isEmpty()) {
            predicates.add(mediaRoot.get("provider").in(providers));
        }

        // Add search filter
        if (search != null && !search.isEmpty()) {
            predicates.add(criteriaBuilder.like(mediaRoot.get("name"), "%" + search + "%"));
        }

        // Add participant filter (assuming 'participant' is a column in the Media table)
        if (participant != null && !participant.isEmpty()) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(mediaRoot.get("actors"), "%" + participant + "%"),
                    criteriaBuilder.like(mediaRoot.get("creators"), "%" + participant + "%"),
                    criteriaBuilder.like(mediaRoot.get("directors"), "%" + participant + "%")
            ));
        }

        // Apply all predicates as an 'AND' operation
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // Apply sorting
        if (isOrderValid(orderBy) && isSortOrderValid(sortOrder)) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(mediaRoot.get(orderBy)));
            } else if ("desc".equalsIgnoreCase(sortOrder)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(mediaRoot.get(orderBy)));
            }
        }

        TypedQuery<Media> query = em.createQuery(criteriaQuery);

        // Set paging
        query.setFirstResult((pageNumber - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
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
