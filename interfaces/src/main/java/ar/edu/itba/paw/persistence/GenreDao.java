package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.MovieGenre;
import ar.edu.itba.paw.models.Genre.TVGenre;

import java.util.Optional;

public interface GenreDao {

    Optional<MovieGenre> getGenreForMovie(int movieId);
    Optional<TVGenre> getGenreForTvSerie(int tvId);

}
