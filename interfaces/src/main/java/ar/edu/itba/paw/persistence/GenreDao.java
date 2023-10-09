package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;


public interface GenreDao {
    List<String> getAllGenres();
    List<String> getGenresForMedia(int mediaId);
}
