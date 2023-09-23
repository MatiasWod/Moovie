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
    public List<Media> getMoovieList(int size, int pageNumber){
        return mediaDao.getMoovieList(size, pageNumber);
    }

    @Override
    public Optional<Integer> getMediaCount() {
        return mediaDao.getMediaCount();
    }

    @Override
    public List<Media> getMediaOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return mediaDao.getMediaOrderedByTmdbRatingDesc(size, pageNumber);
    }

    @Override
    public List<Media> getMediaOrderedByReleaseDateDesc(int size, int pageNumber) {
        return mediaDao.getMediaOrderedByReleaseDateDesc(size, pageNumber);
    }

    @Override
    public List<Media> getMediaFilteredByGenre(String genre, int size, int pageNumber) {
        return mediaDao.getMediaFilteredByGenre(genre, size, pageNumber);
    }

    @Override
    public List<Media> getMediaFilteredByGenreList(List<String> genres, int size, int pageNumber){
        return mediaDao.getMediaFilteredByGenreList(genres,size,pageNumber);
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        return mediaDao.getMovieById(mediaId);
    }

    @Override
    public List<Media> getMediaBySearch(String searchString, int size, int pageNumber) {
        return mediaDao.getMediaBySearch(searchString, size, pageNumber);
    }

    @Override
    public List<Media> getMediaByMoovieListId(int moovieListId, int size, int pageNumber){
        return mediaDao.getMediaByMoovieListId(moovieListId, size, pageNumber);
    }

    @Override
    public List<Movie> getMovieList(int size, int pageNumber) {
        return mediaDao.getMovieList(size, pageNumber);
    }

    @Override
    public Optional<Integer> getMovieCount() {
        return mediaDao.getMovieCount();
    }

    @Override
    public List<Movie> getMovieOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return mediaDao.getMovieOrderedByTmdbRatingDesc(size, pageNumber);
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDateDesc(int size, int pageNumber) {
        return mediaDao.getMovieOrderedByReleaseDateDesc(size, pageNumber);
    }

    @Override
    public List<Movie> getMovieFilteredByGenre(String genre, int size, int pageNumber) {
        return mediaDao.getMovieFilteredByGenre(genre, size, pageNumber);
    }

    @Override
    public List<Movie> getMovieOrderedByReleaseDuration(int size, int pageNumber) {
        return mediaDao.getMovieOrderedByReleaseDuration(size, pageNumber);
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        return mediaDao.getTvById(mediaId);
    }

    @Override
    public List<TVSerie> getTvList(int size, int pageNumber) {
        return mediaDao.getTvList(size, pageNumber);
    }

    @Override
    public Optional<Integer> getTvCount() {
        return mediaDao.getTvCount();
    }

    @Override
    public List<TVSerie> getTvOrderedByTmdbRatingDesc(int size, int pageNumber) {
        return mediaDao.getTvOrderedByTmdbRatingDesc(size, pageNumber);
    }

    @Override
    public List<TVSerie> getTvOrderedByReleaseDateDesc(int size, int pageNumber) {
        return mediaDao.getTvOrderedByReleaseDateDesc(size, pageNumber);
    }

    @Override
    public List<TVSerie> getTvFilteredByGenre(String genre, int size, int pageNumber) {
        return mediaDao.getTvFilteredByGenre(genre, size, pageNumber);
    }
}