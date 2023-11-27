package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.persistence.MediaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;


@Service
public class MediaServiceImpl implements MediaService{
    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber){
        return mediaDao.getMedia(type, search, participant,  genres, providers, status, lang, orderBy, sortOrder, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return mediaDao.getMediaInMoovieList(moovieListId, size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Media getMediaById(int mediaId) {
        return mediaDao.getMediaById(mediaId).orElseThrow(() -> new MediaNotFoundException("Media was not found for the id:" + mediaId));
    }

    @Transactional(readOnly = true)
    @Override
    public Movie getMovieById(int mediaId) {
        return mediaDao.getMovieById(mediaId).orElseThrow(() -> new MediaNotFoundException("Movie was not found for the id:" + mediaId));
    }

    @Transactional(readOnly = true)
    @Override
    public TVSerie getTvById(int mediaId) {
        return mediaDao.getTvById(mediaId).orElseThrow(() -> new MediaNotFoundException("Tv was not found for the id:" + mediaId));
    }

    @Override
    public int getDirectorsForQueryCount(String query, int size) {
        return mediaDao.getDirectorsForQueryCount(query, size);
    }

    @Override
    public List<Director> getDirectorsForQuery(String query, int size) {
        return mediaDao.getDirectorsForQuery(query, size);
    }


    @Transactional(readOnly = true)
    @Override
    public int getMediaCount(int type, String search, String participant, List<String> genres, List<String> providers, List<String> status, List<String> lang) {
        return mediaDao.getMediaCount(type, search, participant,  genres,  providers, status,  lang);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Movie> getMediaForDirectorId(int directorId) {
        return mediaDao.getMediaForDirectorId(directorId);
    }
}