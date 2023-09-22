package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaDao {
    Optional<Media> getMediaById(int mediaId);
    List<Media> getMoovieList(int size, int pageNumber);
    Optional<Integer> getMediaCount();
    List<Media> getMediaOrderedByTmdbRatingDesc(int size, int pageNumber);
    List<Media> getMediaOrderedByReleaseDateDesc(int size, int pageNumber);
    List<Media> getMediaFilteredByGenre(String genre, int size, int pageNumber);
    List<Media> getMediaBySearch(String searchString, int size, int pageNumber);
    List<Media> getMediaByMoovieListId(int moovieListId, int size, int pageNumber);

    Optional<Movie> getMovieById(int mediaId);
    List<Movie> getMovieList();
    Optional<Integer> getMovieCount();
    public List<Movie> getMovieOrderedByTmdbRatingDesc();
    public List<Movie> getMovieOrderedByReleaseDateDesc();
    public List<Movie> getMovieFilteredByGenre(String genre);
    public List<Movie> getMovieOrderedByReleaseDuration();

    Optional<TVSerie> getTvById(int mediaId);
    List<TVSerie> getTvList();
    Optional<Integer> getTvCount();
    public List<TVSerie> getTvOrderedByTmdbRatingDesc();
    public List<TVSerie> getTvOrderedByReleaseDateDesc();
    public List<TVSerie> getTvFilteredByGenre(String genre);
}
