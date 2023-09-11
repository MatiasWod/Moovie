package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.MovieGenre;
import ar.edu.itba.paw.models.Genre.TVGenre;

import java.util.Optional;

public interface GenreService {
    Optional<MovieGenre> getGenreForMovie(int movieId);
    Optional<TVGenre> getGenreForTvSerie(int tvId);
}
