package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaDao {
    public List<Media> getMedia(int type, String search, List<String> genres, String actor, String director, String creator, String orderBy, int size, int pageNumber);

    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    Optional<Media> getMediaById(int mediaId);
    Optional<Movie> getMovieById(int mediaId);
    Optional<TVSerie> getTvById(int mediaId);
    int getMediaCount(int type, String search, List<String> genres, String actor, String director, String creator);
}
