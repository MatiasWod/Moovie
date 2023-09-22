package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaService {

    public static final int DEFAULT_PAGE_SIZE = 25;

    Optional<Media> getMediaById(int mediaId);
    List<Media> getMoovieList(int size, int pageNumber);
    Optional<Integer> getMediaCount();
    List<Media> getMediaOrderedByTmdbRatingDesc(int size, int pageNumber);
    List<Media> getMediaOrderedByReleaseDateDesc(int size, int pageNumber);
    List<Media> getMediaFilteredByGenre(String genre, int size, int pageNumber);
    List<Media> getMediaBySearch(String searchString, int size, int pageNumber);
    List<Media> getMediaByMoovieListId(int moovieListId, int size, int pageNumber);

    Optional<Movie> getMovieById(int mediaId);
    List<Movie> getMovieList(int size, int pageNumber);
    Optional<Integer> getMovieCount();
    public List<Movie> getMovieOrderedByTmdbRatingDesc(int size, int pageNumber);
    public List<Movie> getMovieOrderedByReleaseDateDesc(int size, int pageNumber);
    public List<Movie> getMovieFilteredByGenre(String genre, int size, int pageNumber);
    public List<Movie> getMovieOrderedByReleaseDuration(int size, int pageNumber);

    Optional<TVSerie> getTvById(int mediaId);
    List<TVSerie> getTvList(int size, int pageNumber);
    Optional<Integer> getTvCount();
    public List<TVSerie> getTvOrderedByTmdbRatingDesc(int size, int pageNumber);
    public List<TVSerie> getTvOrderedByReleaseDateDesc(int size, int pageNumber);
    public List<TVSerie> getTvFilteredByGenre(String genre, int size, int pageNumber);
}
