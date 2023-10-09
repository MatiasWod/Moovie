package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.persistence.MediaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MediaServiceImpl implements MediaService{
    @Autowired
    private MediaDao mediaDao;

    @Override
    public List<Media> getMedia(int type, String search, List<String> genres, String orderBy, int size, int pageNumber) {
        return mediaDao.getMedia(type, search, genres, orderBy, size, pageNumber);
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
}