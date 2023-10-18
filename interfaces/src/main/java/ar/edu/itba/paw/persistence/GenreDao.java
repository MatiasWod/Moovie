package ar.edu.itba.paw.persistence;

import java.util.List;


public interface GenreDao {
    List<String> getAllGenres();
    List<String> getGenresForMedia(int mediaId);
}
