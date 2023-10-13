package ar.edu.itba.paw.models.Media;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class extendedMedia extends Media{
    private final List<String> genres;

    private final List<String> providerNames;

    private final List<String> providerLogos;

    public extendedMedia(int mediaId, boolean type, String name, String originalLanguage,
                         boolean adult, Date releaseDate, String overview,
                         String backdropPath, String posterPath, String trailerLink,
                         float tmdbRating, int totalRating, int voteCount, String status,
                         String genres, String providerNames, String providerLogos) {
        super(mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, totalRating, voteCount, status);

        if(genres!=null){
            String[] aux = genres.replaceAll("[{}]","").split(",");
            this.genres = new ArrayList<>(Arrays.asList(aux));
        }else{
            this.genres = new ArrayList<>();
        }

        if(providerNames!=null){
            String[] aux = providerNames.replaceAll("[{}]","").split(",");
            this.providerNames = new ArrayList<>(Arrays.asList(aux));
        }else{
            this.providerNames = new ArrayList<>();
        }

        if(providerLogos!=null){
            String[] aux = providerLogos.replaceAll("[{}]","").split(",");
            this.providerLogos = new ArrayList<>(Arrays.asList(aux));
        }else{
            this.providerLogos = new ArrayList<>();
        }
    }

    public List<String> getProviderLogos() {
        return providerLogos;
    }

    public List<String> getProviderNames() {
        return providerNames;
    }

    public List<String> getGenres() {
        return genres;
    }
}
