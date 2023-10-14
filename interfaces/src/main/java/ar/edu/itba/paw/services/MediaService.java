package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;

public interface MediaService {

    // Returns a list of media that satisfy the conditions
    List<Media> getMedia(int type, String search, String participant, List<String> genres, String orderBy, String sortOrder, int size, int pageNumber);

    //The amount of results that a query of getMedia will give
    int getMediaCount(int type, String search, List<String> genres);

    //Return a list of media that are in a moovie list
    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    //Get the Tv or Movie details data
    Media getMediaById(int mediaId);
    Movie getMovieById(int mediaId);
    TVSerie getTvById(int mediaId);

    void upMediaVoteCount(int mediaId);

    void downMediaVoteCount(int mediaId);

    }