package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.Optional;

public interface GenreService {
    Optional<Genre> getGenreForMedia(int mediaId);
}
