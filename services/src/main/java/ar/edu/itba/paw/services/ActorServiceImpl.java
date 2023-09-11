package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Cast.MovieActor;
import ar.edu.itba.paw.models.Cast.TVActor;
import ar.edu.itba.paw.persistence.ActorDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ActorServiceImpl implements ActorService{
    @Autowired
    private ActorDao actorDao;

    @Override
    public List<MovieActor> getAllActorsForMovie(int movieId) {
        return actorDao.getAllActorsForMovie(movieId);
    }

    @Override
    public List<TVActor> getAllActorsForTvSerie(int tvId) {
        return actorDao.getAllActorsForTvSerie(tvId);
    }

    @Override
    public Optional<Integer> getNumberOfMovies() {
        return actorDao.getNumberOfMovies();
    }

    @Override
    public Optional<Integer> getNumberOfTvSeries() {
        return actorDao.getNumberOfTvSeries();
    }
}
