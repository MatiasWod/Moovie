package ar.edu.itba.paw.services;

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
    public Optional<Media> getMediaById(int mediaId) {
        return mediaDao.getMediaById(mediaId);
    }

    @Override
    public List<Media> getMediaList() {
        return mediaDao.getMediaList();
    }

    @Override
    public Optional<Integer> getMediaCount() {
        return mediaDao.getMediaCount();
    }

    @Override
    public List<Media> getMediaOrderedByTmdbRatingDesc() {
        return mediaDao.getMediaOrderedByTmdbRatingDesc();
    }

    @Override
    public List<Media> getMediaOrderedByReleaseDateDesc() {
        return mediaDao.getMediaOrderedByReleaseDateDesc();
    }

    @Override
    public List<Media> getMediaFilteredByGenre(String genre) {
        return mediaDao.getMediaFilteredByGenre(genre);
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return mediaDao.getMovieById(mediaId);
    }

    @Override
    public List<Media> getMediaBySearch(String searchString) {
        return mediaDao.getMediaBySearch(searchString);
    }

    @Override
    public List<Media> getMediaByMediaListId(int mediaListId){
        return mediaDao.getMediaByMediaListId(mediaListId);
    }

    @Override
    public List<Movie> getMovieList() {
        return mediaDao.getMovieList();
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return mediaDao.getMovieCount();
    }

    @Override
    public List<Movie> getMovieOrderedByTmdbRatingDesc() {
        return mediaDao.getMovieOrderedByTmdbRatingDesc();
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDateDesc() {
        return mediaDao.getMovieOrderedByReleaseDateDesc();
    }

    @Override
    public List<Movie> getMovieFilteredByGenre(String genre) {
        return mediaDao.getMovieFilteredByGenre(genre);
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDuration() {
        return mediaDao.getMovieOrderedByReleaseDuration();
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return mediaDao.getTvById(mediaId);
    }

    @Override
    public List<TVSerie> getTvList() {
        return mediaDao.getTvList();
    }

    @Override
    public Optional<Integer> getTvCount() {
        return mediaDao.getTvCount();
    }

    @Override
    public List<TVSerie> getTvOrderedByTmdbRatingDesc() {
        return mediaDao.getTvOrderedByTmdbRatingDesc();
    }

    @Override
    public List<TVSerie> getTvOrderedByReleaseDateDesc() {
        return mediaDao.getTvOrderedByReleaseDateDesc();
    }

    @Override
    public List<TVSerie> getTvFilteredByGenre(String genre) {
        return mediaDao.getTvFilteredByGenre(genre);
    }
}