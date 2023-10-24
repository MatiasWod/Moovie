package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="movies")
public class Movie extends Media{

    @Column
    private final int runtime;

    @Column
    private final long budget;

    @Column
    private final long revenue;

    @Column
    private int directorId;

    @Column(length = 255)
    private final String director;

    public Movie(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview, String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount, String status, List<String> genres, List<Provider> providers, int runtime, long budget, long revenue, int directorId, String director) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status, genres, providers);
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
        this.directorId = directorId;
        this.director = director;
    }

    public int getRuntime() {
        return runtime;
    }

    public long getBudget() {
        return budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getDirector() {
        return director;
    }
}
