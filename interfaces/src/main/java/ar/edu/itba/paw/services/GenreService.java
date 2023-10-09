package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;

public interface GenreService {
    List<String> getAllGenres();
    List<String> getGenresForMedia(int mediaId)
}
