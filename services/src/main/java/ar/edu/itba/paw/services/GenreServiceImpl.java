package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.persistence.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{
    @Autowired
    private GenreDao genreDao;
    
    @Override
    public List<String> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Override
    public List<String> getGenresForMedia(int mediaId) {
        return genreDao.getGenresForMedia(mediaId);
    }
}
