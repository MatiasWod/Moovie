package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.MovieGenre;
import ar.edu.itba.paw.models.Genre.TVGenre;
import ar.edu.itba.paw.persistence.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService{
    @Autowired
    private GenreDao genreDao;

    @Override
    public Optional<MovieGenre> getGenreForMovie(int movieId) {
        return genreDao.getGenreForMovie(movieId);
    }

    @Override
    public Optional<TVGenre> getGenreForTvSerie(int tvId) {
        return genreDao.getGenreForTvSerie(tvId);
    }
}
