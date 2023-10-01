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
    List<Integer> getMediaIdOrderedByTmdbRatingDesc(int size, int pageNumber);
    List<Media> getMediaOrderedByReleaseDateDesc(int size, int pageNumber);
    List<Media> getMediaFilteredByGenre(String genre, int size, int pageNumber);
    List<Media> getMediaFilteredByGenreList(List<String> genres, int size, int pageNumber);
    Optional<Integer> getMediaFilteredByGenreListCount(List<String> genres);
    List<Media> getMediaBySearch(String searchString, int size, int pageNumber);
    List<Media> getMediaByMoovieListId(int moovieListId, int size, int pageNumber);
    List<Media> getMoovieListContentByIdMediaBUpTo(int moovieListId, int to);

    Optional<Movie> getMovieById(int mediaId);
    List<Movie> getMovieList(int size, int pageNumber);
    Optional<Integer> getMovieCount();
    public List<Movie> getMovieOrderedByTmdbRatingDesc(int size, int pageNumber);
    public List<Movie> getMovieOrderedByReleaseDateDesc(int size, int pageNumber);
    public List<Movie> getMovieFilteredByGenre(String genre, int size, int pageNumber);
    public List<Movie> getMovieFilteredByGenreList(List<String> genres, int size, int pageNumber);
    Optional<Integer> getMovieFilteredByGenreListCount(List<String> genres);
    public List<Movie> getMovieOrderedByReleaseDuration(int size, int pageNumber);

    Optional<TVSerie> getTvById(int mediaId);
    List<TVSerie> getTvList(int size, int pageNumber);
    Optional<Integer> getTvCount();
    public List<TVSerie> getTvOrderedByTmdbRatingDesc(int size, int pageNumber);
    public List<TVSerie> getTvOrderedByReleaseDateDesc(int size, int pageNumber);
    public List<TVSerie> getTvFilteredByGenre(String genre, int size, int pageNumber);
    public List<TVSerie> getTvFilteredByGenreList(List<String> genres, int size, int pageNumber);
    Optional<Integer> getTvFilteredByGenreListCount(List<String> genres);
}
