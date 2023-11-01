package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class MediaHibernateDao implements MediaDao{

    @PersistenceContext
    private EntityManager em;

    private static final String moviesQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status, runtime, budget, revenue, directorId, director ";

    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, String orderBy, String sortOrder, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT m.mediaid ");
        ArrayList<String> argtype = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();

        sql.append("FROM media m LEFT JOIN reviews r ON m.mediaid = r.mediaid ");

        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (type == 0 || type == 1) {
            sql.append(" WHERE type = :type ");
            argtype.add("type");
            args.add(type == 1);
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }

        // Add the genres filter
        if (genres != null && !genres.isEmpty()) {
            sql.append(" AND m.mediaId IN (");
            sql.append(" SELECT mg.mediaId FROM mediagenres mg");
            sql.append(" JOIN genres g ON g.genreId = mg.genreId");
            sql.append(" WHERE g.genreName IN (:genres)");
            sql.append(" ) ");
            argtype.add("genres");
            args.add(genres);
        }

        // Add the providers filter
        if (providers!=null && !providers.isEmpty()) {
            sql.append(" AND m.mediaId IN (");
            sql.append(" SELECT mp.mediaId FROM mediaproviders mp");
            sql.append(" JOIN providers p ON p.providerId = mp.providerId");
            sql.append(" WHERE p.providerName IN (:providers)");
            sql.append(" ) ");
            argtype.add("providers");
            args.add(providers);
        }

        //Input the search
        if(search!=null && search.length()>0){
            sql.append(" AND " );
            sql.append(" name ILIKE :name ");
            argtype.add("name");
            args.add('%' + search + '%');
        }

        // Input its participants in actors, media.name, creators and directors
        if (participant!=null && participant.length()>0) {
            sql.append(" AND  " );
            sql.append(" (  m.mediaId IN (SELECT mediaid FROM actors a WHERE actorname ILIKE :actor ) ");
            args.add('%' + participant + '%');
            argtype.add("actor");

            if(type != MediaTypes.TYPE_TVSERIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM movies m WHERE director ILIKE :director ) ");
                args.add('%' + participant + '%');
                argtype.add("director");
            }

            if(type != MediaTypes.TYPE_MOVIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM creators c WHERE creatorname ILIKE :creator ) ");
                args.add('%' + participant + '%');
                argtype.add("creator");
            }

            sql.append(" ) ");
        }

        sql.append("GROUP BY m.mediaId ");

        // Order by
        if (isOrderValid(orderBy) && isSortOrderValid(sortOrder)) {
            sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append(sortOrder);
        }

        // Pagination
        sql.append(" LIMIT :size OFFSET :page "); // Add LIMIT and OFFSET clauses
        argtype.add("size");
        args.add(size);
        argtype.add("page");
        args.add(pageNumber * size);

        Query nq = em.createNativeQuery(sql.toString());

        for(int i=0; i<args.size() ; i++){
            nq.setParameter(argtype.get(i), args.get(i));
        }

        List<Integer> ids = nq.getResultList();

        final TypedQuery<Media> query = em.createQuery("from Media m where m.mediaId in (:ids)", Media.class);
        query.setParameter("ids", ids);

        return query.getResultList();
    }

    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {

        return em.createQuery("SELECT m FROM MoovieListContentEntity mlcE INNER JOIN Media m ON mlcE.mediaId = m.mediaId " +
                        "WHERE mlcE.moovieListId = :moovieListId", Media.class)
                .setParameter("moovieListId", moovieListId)
                .setFirstResult(size*pageNumber).setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<Media> getMediaById(int mediaId) {
        return Optional.ofNullable(em.find(Media.class, mediaId));
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
//        final Query baseQuery = em.createNativeQuery("SELECT " + moviesQueryParams +
//                ",(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = media.mediaId) AS genres, "
//                + "(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = media.mediaId) AS providers, "
//                + "AVG(rating) AS totalrating, COUNT(rating) AS votecount  "
//                + "FROM Media media LEFT JOIN Reviews r ON media.mediaId = r.mediaId WHERE media.mediaId = :mediaId "
//                + "GROUP BY media.mediaId, "
//                + moviesQueryParams).setParameter("mediaId",mediaId);
//        Movie movie= (Movie) baseQuery.getSingleResult();
        final Movie aux = em.createQuery("SELECT m FROM Movie m WHERE m.mediaId = :mediaId", Movie.class)
                .setParameter("mediaId",mediaId)
                .getSingleResult();
        return Optional.ofNullable(aux);
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {

        final TVSerie aux = em.createQuery("SELECT tv FROM TVSerie tv WHERE tv.mediaId = :mediaId", TVSerie.class)
                .setParameter("mediaId",mediaId)
                .getSingleResult();
        return Optional.ofNullable(aux);
    }

    @Override
    public int getMediaCount(int type, String search, String participant, List<String> genres, List<String> providers){
        StringBuilder sql = new StringBuilder("SELECT m.mediaid ");
        ArrayList<String> argtype = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();

        sql.append("FROM media m LEFT JOIN reviews r ON m.mediaid = r.mediaid ");

        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (type == 0 || type == 1) {
            sql.append(" WHERE type = :type ");
            argtype.add("type");
            args.add(type == 1);
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }

        // Add the genres filter
        if (genres != null && !genres.isEmpty()) {
            sql.append(" AND m.mediaId IN (");
            sql.append(" SELECT mg.mediaId FROM mediagenres mg");
            sql.append(" JOIN genres g ON g.genreId = mg.genreId");
            sql.append(" WHERE g.genreName IN (:genres)");
            sql.append(" ) ");
            argtype.add("genres");
            args.add(genres);
        }

        // Add the providers filter
        if (providers!=null && !providers.isEmpty()) {
            sql.append(" AND m.mediaId IN (");
            sql.append(" SELECT mp.mediaId FROM mediaproviders mp");
            sql.append(" JOIN providers p ON p.providerId = mp.providerId");
            sql.append(" WHERE p.providerName IN (:providers)");
            sql.append(" ) ");
            argtype.add("providers");
            args.add(providers);
        }

        //Input the search
        if(search!=null && search.length()>0){
            sql.append(" AND " );
            sql.append(" name ILIKE :name ");
            argtype.add("name");
            args.add('%' + search + '%');
        }

        // Input its participants in actors, media.name, creators and directors
        if (participant!=null && participant.length()>0) {
            sql.append(" AND  " );
            sql.append(" (  m.mediaId IN (SELECT mediaid FROM actors a WHERE actorname ILIKE :actor ) ");
            args.add('%' + participant + '%');
            argtype.add("actor");

            if(type != MediaTypes.TYPE_TVSERIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM movies m WHERE director ILIKE :director ) ");
                args.add('%' + participant + '%');
                argtype.add("director");
            }

            if(type != MediaTypes.TYPE_MOVIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM creators c WHERE creatorname ILIKE :creator ) ");
                args.add('%' + participant + '%');
                argtype.add("creator");
            }

            sql.append(" ) ");
        }

        sql.append("GROUP BY m.mediaid ");

        Query nq = em.createNativeQuery(sql.toString());

        for(int i=0; i<args.size() ; i++){
            nq.setParameter(argtype.get(i), args.get(i));
        }

        return nq.getResultList().size();
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
