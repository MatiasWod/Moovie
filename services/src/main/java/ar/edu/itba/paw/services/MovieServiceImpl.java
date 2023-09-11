package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.persistence.MovieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService{
    @Autowired
    private MovieDao movieDao;

    @Override
    public Optional<Movie> getById(int movieId) {
        return movieDao.getById(movieId);
    }

    @Override
    public List<Movie> getMovieList() {
        return movieDao.getMovieList();
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return movieDao.getMovieCount();
    }
}
