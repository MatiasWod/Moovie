package ar.edu.itba.paw.models.Media;

import ar.edu.itba.paw.models.Provider.Provider;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Media {
    private final int mediaId;
    private final boolean type;
    private final String name;
    private final String originalLanguage;
    private final boolean adult;
    private final Date releaseDate;
    private String overview;
    private final String backdropPath;
    private final String posterPath;
    private final String trailerLink;
    private final float tmdbRating;
    private final int totalRating;//total de todas las ratings
    private final int voteCount;//cantidad de gente que votó
    private final String status;
    private final List<String> genres;

    private final List<Provider> providers;


    public Media(int mediaId, boolean type, String name, String originalLanguage, boolean adult, Date releaseDate, String overview,
                 String backdropPath, String posterPath, String trailerLink, float tmdbRating, int totalRating, int voteCount, String status,
                 String genres, String providers) {
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

        if(genres!=null){
            String[] aux = genres.replaceAll("[{}]","").split(",");
            this.genres = new ArrayList<>(Arrays.asList(aux));
        }else{
            this.genres = new ArrayList<>();
        }

        if(providers!=null){
            this.providers = new ArrayList<Provider>();
            String[] aux = providers.replaceAll("[({\"\\\\})]","").split(",");
            for(int i=0 ; i  < aux.length ; i+=4 ){
                this.providers.add(new Provider( Integer.parseInt(aux[i+1]) , aux[i+2], aux[i+3]));
            }
        }else{
            this.providers = new ArrayList<>();
        }

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

    public int getTotalRating() {
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
        return genres;
    }

    public List<Provider> getProviders() {
        return providers;
    }

}
