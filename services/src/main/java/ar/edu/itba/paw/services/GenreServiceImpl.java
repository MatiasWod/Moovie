package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.persistence.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService{
    @Autowired
    private GenreDao genreDao;

    @Override
    public Optional<Genre> getGenreForMedia(int mediaId) {
        return genreDao.getGenreForMedia(mediaId);
    }

}
