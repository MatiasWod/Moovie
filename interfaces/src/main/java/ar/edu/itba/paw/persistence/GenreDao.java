package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.Optional;

public interface GenreDao {

    Optional<Genre> getGenreForMedia(int mediaId);

}
