package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaService {
    static final int TYPE_MOVIE = 0;
    static final int TYPE_TVSERIE = 1;
    static final int TYPE_ALL = 2;

    public static final int DEFAULT_PAGE_SIZE = 25;

    // Returns a list of media that satisfy the conditions
    List<Media> getMedia(int type, String search, List<String> genres, String orderBy, int size, int pagNumber);

    //Return a list of media that are in a moovie list
    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    //Get the Tv or Movie details data
    //TODO add actors and proviers to the querys
    Movie getMovieById(int mediaId);
    TVSerie getTvById(int mediaId);
}