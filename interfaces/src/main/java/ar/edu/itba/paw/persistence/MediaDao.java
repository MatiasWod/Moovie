package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaDao {
    List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber, int currentUserId);

    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    Optional<Media> getMediaById(int mediaId);
    Optional<Movie> getMovieById(int mediaId);
    Optional<TVSerie> getTvById(int mediaId);
    int getMediaCount(int type, String search, String participant, List<String> genres, List<String> providers, List<String> status, List<String> lang);

}
