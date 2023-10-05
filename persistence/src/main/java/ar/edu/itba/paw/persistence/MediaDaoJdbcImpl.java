package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public class MediaDaoJdbcImpl implements MediaDao {
    private final JdbcTemplate jdbcTemplate;

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
            rs.getString("status")
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
            rs.getString("director")
    );

    private static final String moviesQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, runtime, budget, revenue, directorId, director ";

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
            rs.getInt("numberOfSeasons")
    );

    private static final String tvQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, lastAirDate, nextEpisodeToAir, numberOfEpisodes, numberOfSeasons ";


    private static final RowMapper<Integer> COUNT_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("count"));

    private static final RowMapper<Integer> MEDIA_ID_ROW_MAPPER = ((resultSet, i) -> resultSet.getInt("mediaId"));

    @Autowired
    public MediaDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Media> getMedia(int type, String search, List<String> genres,
                                String orderBy, int size, int pageNumber) {
        StringBuilder sql = new StringBuilder("SELECT * FROM media ");
        ArrayList<Object> args = new ArrayList<>();

        // If type is 0 or 1 it's specifically movies or TVs, else it's not restricted
        if (type == 0 || type == 1) {
            sql.append(" WHERE type = ? ");
            args.add(type == 1);
        } else {
            sql.append(" WHERE type NOT NULL ");
        }

        // Input the search
        if (search!=null && search.length()>0) {
            sql.append(" AND name ILIKE ? ");
            args.add('%' + search + '%');
        }

        // Add the genres filter
        if (genres!=null && !genres.isEmpty()) {
            sql.append(" AND mediaId IN ( SELECT mediaId FROM genres WHERE "); // Start the OR conditions for genres
            for (String genre : genres) {
                sql.append(" genre_column = ? OR "); // Replace 'genre_column' with your actual genre column name
                args.add(genre);
            }
            sql.deleteCharAt(sql.length());
            sql.deleteCharAt(sql.length());
            sql.deleteCharAt(sql.length());
            sql.append(" GROUP BY mediaId HAVING COUNT(*) >= 2) ");
        }

        // Order by
        if (orderBy!=null && orderBy.length()>0) {
            sql.append(" ORDER BY ? ");
            args.add(orderBy);
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
        return jdbcTemplate.query("SELECT * FROM media  WHERE mediaid = ?", new Object[]{mediaId}, MEDIA_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid WHERE  movies.mediaid = ?", new Object[]{mediaId}, MOVIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid WHERE  tv.mediaid = ?", new Object[]{mediaId}, TV_SERIE_ROW_MAPPER).stream().findFirst();
    }


    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT media.* FROM moovieListscontent INNER JOIN media ON media.mediaId = moovieListscontent.mediaid WHERE moovielistscontent.moovieListId = ? ORDER BY media.mediaId LIMIT ? OFFSET ?", new Object[]{moovieListId, size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

}
