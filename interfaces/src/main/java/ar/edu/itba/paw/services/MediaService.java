package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    Optional<Media> getMediaById(int mediaId);
    List<Media> getMediaList();
    Optional<Integer> getMediaCount();

    Optional<Movie> getMovieById(int mediaId);
    List<Movie> getMovieList();
    Optional<Integer> getMovieCount();

    Optional<TVSerie> getTvById(int mediaId);
    List<TVSerie> getTvList();
    Optional<Integer> getTvCount();
}
