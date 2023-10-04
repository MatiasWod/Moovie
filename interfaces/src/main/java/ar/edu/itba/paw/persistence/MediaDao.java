package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaDao {
    List<Media> getMedia(int type, String search, List<String> genres, String orderBy, int size, int pagNumber);

    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    Optional<Movie> getMovieById(int mediaId);
    Optional<TVSerie> getTvById(int mediaId);
}
