package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres(int pageSize, int pageNumber);
    int getAllGenresCount();
    List<Genre> getGenresForMedia(int mediaId, int pageSize, int pageNumber);
    int getGenresForMediaCount(int mediaId);
    Genre getGenreById (int genreId);
}
