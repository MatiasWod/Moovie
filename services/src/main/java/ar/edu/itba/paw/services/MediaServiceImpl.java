package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.persistence.MediaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MediaServiceImpl implements MediaService{
    @Autowired
    private MediaDao mediaDao;

    @Override
    public List<Media> getMedia(int type, String search, String participant, List<String> genres, String orderBy, String sortOrder, int size, int pageNumber){
        return mediaDao.getMedia(type, search, participant,  genres, orderBy, sortOrder, size, pageNumber);
    }

    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {
        return mediaDao.getMediaInMoovieList(moovieListId, size, pageNumber);
    }

    @Override
    public Media getMediaById(int mediaId) {
        return mediaDao.getMediaById(mediaId).orElseThrow(() -> new MediaNotFoundException("Media was not found for the id:" + mediaId));
    }

    @Override
    public Movie getMovieById(int mediaId) {
        return mediaDao.getMovieById(mediaId).orElseThrow(() -> new MediaNotFoundException("Movie was not found for the id:" + mediaId));
    }

    @Override
    public TVSerie getTvById(int mediaId) {
        return mediaDao.getTvById(mediaId).orElseThrow(() -> new MediaNotFoundException("Tv was not found for the id:" + mediaId));
    }

    @Override
    public int getMediaCount(int type, String search, List<String> genres) {
        return mediaDao.getMediaCount(type,search,genres);
    }

    @Override
    public void upMediaVoteCount(int mediaId) {
        mediaDao.upMediaVoteCount(mediaId);
    }

    @Override
    public void downMediaVoteCount(int mediaId) {
        mediaDao.downMediaVoteCount(mediaId);
    }
}