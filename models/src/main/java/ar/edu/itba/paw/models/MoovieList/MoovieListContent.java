package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Provider.Provider;

import java.util.Date;
import java.util.List;

public class MoovieListContent extends Media {
    private boolean watched;


    public MoovieListContent(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview, String backdropPath, String posterPath, String trailerLink, float tmdbRating, float totalRating, int voteCount, String status, List<Genre> genres, List<Provider> providers, boolean watched) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
        this.watched = watched;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
