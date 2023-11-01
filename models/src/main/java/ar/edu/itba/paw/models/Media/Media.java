package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Provider.Provider;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.JOINED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_mediaid_seq")
    @SequenceGenerator(sequenceName = "media_mediaid_seq", name = "media_mediaid_seq", allocationSize = 1)
    @Column(name = "mediaId")
    private int mediaId;


    @Column(nullable = false)
    private boolean type;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 2)
    private String originalLanguage;

    @Column(nullable = false)
    private boolean adult;

    @Column
    private Date releaseDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String overview;

    @Column(length = 255)
    private String backdropPath;

    @Column(length = 255)
    private String posterPath;

    @Column(length = 255)
    private String trailerLink;

    @Column(nullable = false)
    private float tmdbRating;

    @Column(length = 20, nullable = false)
    private String status;

    @Formula("(SELECT COALESCE(AVG(r.rating), 0) FROM reviews r WHERE mediaid = r.mediaid)")
    private float totalRating;

    @Formula("(SELECT COUNT(r.rating) FROM reviews r WHERE mediaid = r.mediaid)")
    private int voteCount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mediaproviders",
            joinColumns = @JoinColumn(name = "mediaId"),
            inverseJoinColumns = @JoinColumn(name = "providerId"))
    private List<Provider> providers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mediagenres",
            joinColumns = @JoinColumn(name = "mediaId"),
            inverseJoinColumns = @JoinColumn(name = "genreId"))
    private List<Genre> genres = new ArrayList<>();

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }


    /* Just for Hibernate*/
    Media(){

    }

    public Media(final int mediaId, final boolean type, final String name, final String originalLanguage, final boolean adult, final Date releaseDate, final String overview,
                 final String backdropPath, final String posterPath, final String trailerLink, final float tmdbRating, final float totalRating, final int voteCount, final String status,
                 final List<Genre> genres, final List<Provider> providers) {
        this.mediaId = mediaId;
        this.type = type;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating/2;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.status = status;
        this.genres = genres;
        this.providers = providers;
    }

    public int getMediaId() {
        return mediaId;
    }

    public boolean isType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public boolean isAdult() {
        return adult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public float getTmdbRating() {
        return tmdbRating;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getGenres() {
        List<String> genres = new ArrayList<>();
        for (Genre genre : this.genres) {
            genres.add(genre.getGenre());
        }
        return genres;
    }

    public List<Provider> getProviders() {
        return providers;
    }

}
