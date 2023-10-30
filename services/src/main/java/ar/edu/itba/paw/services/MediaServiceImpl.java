package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.persistence.MediaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class MediaServiceImpl implements MediaService{
    @Autowired
    private MediaDao mediaDao;

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, List<String> providers, String orderBy, String sortOrder, int size, int pageNumber){
        return mediaDao.getMedia(type, search, participant,  genres, providers, orderBy, sortOrder, size, pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return mediaDao.getMediaInMoovieList(moovieListId, size, pageNumber);
    }

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public int getMediaCount(int type, String search, String participantSearch, List<String> genres, List<String> providers) {
        return mediaDao.getMediaCount(type, search, participantSearch, genres, providers);
    }
}