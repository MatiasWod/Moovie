package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Optional<Movie> getById(int movieId);

    List<Movie> getMovieList();

    Optional<Integer> getMovieCount();
}
