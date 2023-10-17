package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class MediaDaoJdbcImpl implements MediaDao {
    private final JdbcTemplate jdbcTemplate;

    static final int TYPE_MOVIE = 0;
    static final int TYPE_TVSERIE = 1;
    static final int TYPE_ALL = 2;

    private static final RowMapper<Media> MEDIA_ROW_MAPPER = (rs, rowNum) -> new Media(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getString("genres"),
            rs.getString("providers")
    );

    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = (rs, rowNum) -> new Movie(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getInt("runtime"),
            rs.getLong("budget"),
            rs.getLong("revenue"),
            rs.getInt("directorId"),
            rs.getString("director"),
            rs.getString("genres"),
            rs.getString("providers")
    );

    private static final RowMapper<TVSerie> TV_SERIE_ROW_MAPPER = (rs, rowNum) -> new TVSerie(
            rs.getInt("mediaId"),
            rs.getBoolean("type"),
            rs.getString("name"),
            rs.getString("originalLanguage"),
            rs.getBoolean("adult"),
            rs.getDate("releaseDate"),
            rs.getString("overview"),
            rs.getString("backdropPath"),
            rs.getString("posterPath"),
            rs.getString("trailerLink"),
            rs.getFloat("tmdbRating"),
            rs.getInt("totalRating"),
            rs.getInt("voteCount"),
            rs.getString("status"),
            rs.getDate("lastAirDate"),
            rs.getDate("nextEpisodeToAir"),
            rs.getInt("numberOfEpisodes"),
            rs.getInt("numberOfSeasons"),
            rs.getString("genres"),
            rs.getString("providers")
    );

    private static final String moviesQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status, runtime, budget, revenue, directorId, director ";

    private static final String tvQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status, lastAirDate, nextEpisodeToAir, numberOfEpisodes, numberOfSeasons ";


    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    private static final RowMapper<Integer> MEDIA_ID_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("mediaId"));

    @Autowired
    public MediaDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, String orderBy, String sortOrder, int size, int pageNumber){
        StringBuilder sql = new StringBuilder("SELECT m.*, ");
        ArrayList<Object> args = new ArrayList<>();

        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = m.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = m.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalrating, ");
        sql.append("COUNT(rating) AS votecount ");

        sql.append("FROM media m LEFT JOIN reviews r ON m.mediaid = r.mediaid ");

        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (type == 0 || type == 1) {
            sql.append(" WHERE type = ? ");
            args.add(type == 1);
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }

        // Add the genres filter
        if (genres!=null && !genres.isEmpty()) {
            sql.append(" AND m.mediaId IN ( SELECT mediaId FROM genres WHERE "); // Start the OR conditions for genres
            for (String genre : genres) {
                sql.append(" genre = ? OR "); // Replace 'genre_column' with your actual genre column name
                args.add(genre);
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ");
        }

        // Add the providers filter
        if (providers!=null && !providers.isEmpty()) {
            sql.append(" AND m.mediaId IN ( SELECT mediaId FROM providers WHERE "); // Start the OR conditions for genres
            for (String provider : providers) {
                sql.append(" providername = ? OR "); // Replace 'genre_column' with your actual genre column name
                args.add(provider);
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ");
        }

        //Input the search
        if(search!=null && search.length()>0){
            sql.append(" AND " );
            sql.append(" name ILIKE ? ");
            args.add('%' + search + '%');
        }

        // Input its participants in actors, media.name, creators and directors
        if (participant!=null && participant.length()>0) {
            sql.append(" AND  " );
            sql.append(" (  m.mediaId IN (SELECT mediaid FROM actors a WHERE actorname ILIKE ?) ");
            args.add('%' + participant + '%');

            if(type != MediaTypes.TYPE_TVSERIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM movies m WHERE director ILIKE ? ) ");
                args.add('%' + participant + '%');
            }

            if(type != MediaTypes.TYPE_MOVIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM creators c WHERE creatorname ILIKE ? ) ");
                args.add('%' + participant + '%');
            }

            sql.append(" ) ");
        }

        sql.append("GROUP BY m.mediaid ");

        // Order by
        if (orderBy!=null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
            sql.append(" ").append(sortOrder);
        }

        // Pagination
        sql.append(" LIMIT ? OFFSET ? "); // Add LIMIT and OFFSET clauses
        args.add(size);
        args.add(pageNumber * size);

        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), MEDIA_ROW_MAPPER);
    }


    @Override
    public Optional<Media> getMediaById(int mediaId) {
        StringBuilder sql = new StringBuilder("SELECT m.*, ");
        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = m.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = m.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalrating, COUNT(rating) AS votecount ");
        sql.append(" FROM media m LEFT JOIN reviews r ON m.mediaid = r.mediaid WHERE m.mediaid = ?");
        sql.append(" GROUP BY m.mediaid ");

        return jdbcTemplate.query(sql.toString(), new Object[]{mediaId}, MEDIA_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(moviesQueryParams);
        sql.append(", (SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = media.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = media.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalrating, COUNT(rating) AS votecount ");
        sql.append(" FROM media INNER JOIN movies ON media.mediaid = movies.mediaid LEFT JOIN reviews r ON media.mediaid = r.mediaid  WHERE  movies.mediaid = ?");
        sql.append(" GROUP BY media.mediaid, ");
        sql.append(moviesQueryParams);

        return jdbcTemplate.query(sql.toString(),new Object[]{mediaId}, MOVIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(tvQueryParams);
        sql.append(", (SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = media.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = media.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalrating, COUNT(rating) AS votecount ");
        sql.append(" FROM media INNER JOIN tv ON media.mediaid = tv.mediaid LEFT JOIN reviews r ON media.mediaid = r.mediaid  WHERE  tv.mediaid = ?");
        sql.append(" GROUP BY media.mediaid, ");
        sql.append(tvQueryParams);

        return jdbcTemplate.query(sql.toString(),new Object[]{mediaId}, TV_SERIE_ROW_MAPPER).stream().findFirst();
    }


    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT media.*, ");
        sql.append("(SELECT ARRAY_AGG(g.genre) FROM genres g WHERE g.mediaId = media.mediaId) AS genres, ");
        sql.append("(SELECT ARRAY_AGG(p) FROM providers p WHERE p.mediaId = media.mediaId) AS providers, ");
        sql.append("AVG(rating) AS totalRating, COUNT(rating) AS votecount ");
        sql.append(" FROM moovieListscontent INNER JOIN media ON media.mediaId = moovieListscontent.mediaid  LEFT JOIN reviews r ON media.mediaid = r.mediaid ");
        sql.append("WHERE moovielistscontent.moovieListId = ? GROUP BY media.mediaid ORDER BY media.mediaId LIMIT ? OFFSET ? ");

        return jdbcTemplate.query(sql.toString(),new Object[]{moovieListId, size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public int getMediaCount(int type, String search, String participantSearch, List<String> genres, List<String> providers) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM media m ");
        ArrayList<Object> args = new ArrayList<>();
        Boolean flag = false;

        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (type == MediaTypes.TYPE_MOVIE.getType() || type == MediaTypes.TYPE_TVSERIE.getType()) {
            sql.append(" WHERE type = ? ");
            args.add(type == 1);
        } else {
            sql.append(" WHERE type IS NOT NULL ");
        }


        // Add the genres filter
        if (genres!=null && !genres.isEmpty()) {
            sql.append(" AND mediaId IN ( SELECT mediaId FROM genres WHERE "); // Start the OR conditions for genres
            for (String genre : genres) {
                sql.append(" genre = ? OR ");
                args.add(genre);
            }
            // delete last OR
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ");
        }

        // Add the providers filter
        if (providers!=null && !providers.isEmpty()) {
            sql.append(" AND mediaId IN ( SELECT mediaId FROM providers WHERE "); // Start the OR conditions for providers
            for (String provider : providers) {
                sql.append(" providername = ? OR ");
                args.add(provider);
            }
            // delete last OR
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.deleteCharAt(sql.length() - 1);
            sql.append(" ) ");
        }

        //Input the search
        if(search!=null && !search.isEmpty()){
            sql.append(" AND " );
            sql.append(" name ILIKE ? ");
            args.add('%' + search + '%');
        }

        // Input its participants in actors, media.name, creators and directors
        if (participantSearch!=null && !participantSearch.isEmpty()) {
            sql.append(" AND " );
            sql.append(" ( m.mediaId IN (SELECT mediaid FROM actors a WHERE actorname ILIKE ?) ");
            args.add('%' + participantSearch + '%');

            if(type != MediaTypes.TYPE_TVSERIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM movies m WHERE director ILIKE ? ) ");
                args.add('%' + participantSearch + '%');
            }

            if(type != MediaTypes.TYPE_MOVIE.getType()){
                sql.append(" OR m.mediaId IN (SELECT mediaid FROM creators c WHERE creatorname ILIKE ? ) ");
                args.add('%' + participantSearch + '%');
            }

            sql.append(" ) ");
        }
        sql.append(";");
        // Execute the query
        return jdbcTemplate.query(sql.toString(), args.toArray(), COUNT_ROW_MAPPER).stream().findFirst().get().intValue();
    }

}
