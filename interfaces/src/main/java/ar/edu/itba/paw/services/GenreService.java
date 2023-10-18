package ar.edu.itba.paw.services;

import java.util.List;

public interface GenreService {
    List<String> getAllGenres();
    List<String> getGenresForMedia(int mediaId);
}
