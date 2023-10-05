package ar.edu.itba.paw.models.MoovieList;

import ar.edu.itba.paw.models.Media.Media;

import java.util.Date;

public class MoovieListContent extends Media {
    private final boolean watched;

    public MoovieListContent(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview, String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount, String status, boolean watched) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status);
        this.watched = watched;
    }

    public boolean isWatched() {
        return watched;
    }
}
