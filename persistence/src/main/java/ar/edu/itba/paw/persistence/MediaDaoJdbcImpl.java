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

    @Autowired
    public MediaDaoJdbcImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /*** MEDIA QUERIES*/

    @Override
    public Optional<Media> getMediaById(int mediaId) {
        return jdbcTemplate.query("SELECT * FROM media WHERE mediaId = ?", new Object[]{mediaId}, MEDIA_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Media> getMoovieList(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM media LIMIT ? OFFSET ?", new Object[]{size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getMediaCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM media", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Media> getMediaOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM media ORDER BY tmdbrating DESC LIMIT ? OFFSET ?", new Object[]{size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public List<Media> getMediaOrderedByReleaseDateDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM media ORDER BY releasedate DESC LIMIT ? OFFSET ?", new Object[]{size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public List<Media> getMediaFilteredByGenre(String genre, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT media.* FROM media INNER JOIN genres ON media.mediaid = genres.mediaid WHERE genres.genre = ? LIMIT ? OFFSET ?", new Object[]{genre, size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public List<Media> getMediaFilteredByGenreList(List<String> genres, int size, int pageNumber){
        String inClause = String.join(",", Collections.nCopies(genres.size(), "?"));
        String sql = "SELECT media.* FROM media " +
                "INNER JOIN genres ON media.mediaId = genres.mediaId " +
                "WHERE genres.genre IN (" + inClause + ") " +
                "GROUP BY media.mediaId " +
                "HAVING COUNT(DISTINCT genres.genre) = ? " +
                "LIMIT ? OFFSET ?";

        List<Object> params = new ArrayList<>(genres);
        params.add(genres.size()); // Agregar la cantidad de géneros para la cláusula HAVING
        params.add(size);
        params.add(pageNumber * size);

        return jdbcTemplate.query(sql, params.toArray(), MEDIA_ROW_MAPPER);
    }



    @Override
    public List<Media> getMediaBySearch(String searchString, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT * FROM media WHERE media.name ILIKE ? LIMIT ? OFFSET ?", new Object[]{'%' + searchString + '%', size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }

    @Override
    public List<Media> getMediaByMoovieListId(int moovieListId, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT media.* FROM moovieListscontent INNER JOIN media ON media.mediaId = moovieListscontent.mediaid WHERE moovielistscontent.moovieListId = ? ORDER BY media.mediaId LIMIT ? OFFSET ?", new Object[]{moovieListId, size, pageNumber * size}, MEDIA_ROW_MAPPER);
    }


    /*** MOVIE QUERIES*/

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid WHERE  movies.mediaid = ?", new Object[]{mediaId}, MOVIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Movie> getMovieList(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, MOVIE_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM movies ", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Movie> getMovieOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT" + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid ORDER BY tmdbrating LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDateDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid ORDER BY releasedate DESC LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMovieFilteredByGenre(String genre, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM ((media INNER JOIN movies ON media.mediaid = movies.mediaid) INNER JOIN genres ON media.mediaid = genres.mediaid) WHERE genres.genre = ? LIMIT ? OFFSET ?", new Object[]{genre,  size, pageNumber * size}, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMovieFilteredByGenreList(List<String> genres, int size, int pageNumber){
        String inClause = String.join(",", Collections.nCopies(genres.size(), "?"));
        String sql = "SELECT "+ moviesQueryParams +" FROM " +
                "((media INNER JOIN movies ON media.mediaid = movies.mediaid) INNER JOIN genres ON media.mediaId = genres.mediaId) " +
                "WHERE genres.genre IN (" + inClause + ") " +
                "GROUP BY media.mediaId, movies.runtime, movies.budget, movies.revenue, movies.directorid, movies.director " +
                "HAVING COUNT(DISTINCT genres.genre) = ? " +
                "LIMIT ? OFFSET ?";

        List<Object> params = new ArrayList<>(genres);
        params.add(genres.size()); // Agregar la cantidad de géneros para la cláusula HAVING
        params.add(size);
        params.add(pageNumber * size);

        return jdbcTemplate.query(sql, params.toArray(), MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDuration(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + moviesQueryParams + " FROM media INNER JOIN movies ON media.mediaid = movies.mediaid ORDER BY runtime DESC LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, MOVIE_ROW_MAPPER);
    }


    /*** TV QUERIES*/

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid WHERE  tv.mediaid = ?", new Object[]{mediaId}, TV_SERIE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<TVSerie> getTvList(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, TV_SERIE_ROW_MAPPER);
    }

    @Override
    public Optional<Integer> getTvCount() {
        return jdbcTemplate.query("SELECT COUNT(*) AS count FROM tv", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<TVSerie> getTvOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid ORDER BY tmdbrating DESC LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size}, TV_SERIE_ROW_MAPPER);
    }

    @Override
    public List<TVSerie> getTvOrderedByReleaseDateDesc(int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM media INNER JOIN tv ON media.mediaid = tv.mediaid ORDER BY releasedate DESC LIMIT ? OFFSET ?", new Object[]{ size, pageNumber * size} , TV_SERIE_ROW_MAPPER);
    }

    @Override
    public List<TVSerie> getTvFilteredByGenre(String genre, int size, int pageNumber) {
        return jdbcTemplate.query("SELECT " + tvQueryParams + " FROM ((media INNER JOIN tv ON media.mediaid = tv.mediaid) INNER JOIN genres ON media.mediaid = genres.mediaid) WHERE genres.genre = ? LIMIT ? OFFSET ?", new Object[]{genre, size, pageNumber * size}, TV_SERIE_ROW_MAPPER);
    }
}
