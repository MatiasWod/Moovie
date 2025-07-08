package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;


public interface GenreDao {
    List<Genre> getAllGenres(int pageSize, int pageNumber);
    int getAllGenresCount();
    List<Genre> getGenresForMedia(int mediaId, int pageSize, int pageNumber);
    int getGenresForMediaCount(int mediaId);
    Genre getGenreById (int genreId);
}
