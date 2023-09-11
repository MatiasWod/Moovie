package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Cast.MovieActor;
import ar.edu.itba.paw.models.Cast.TVActor;
import ar.edu.itba.paw.models.Movie.Movie;
import ar.edu.itba.paw.models.TV.TVSerie;

import java.util.List;
import java.util.Optional;

public interface ActorDao {

    List<MovieActor> getAllActorsForMovie(int movieId);
    List<TVActor> getAllActorsForTvSerie(int tvId);//fijarse qué hacer con tvId y movieId
    Optional<Integer> getNumberOfMovies();
    Optional<Integer> getNumberOfTvSeries();
}
