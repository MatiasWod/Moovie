package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Movie.Movie;
import ar.edu.itba.paw.models.TV.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MovieDao {

    Optional<Movie> getById(int movieId);

    List<Movie> getMovieList();


    Optional<Integer> getMovieCount();

}