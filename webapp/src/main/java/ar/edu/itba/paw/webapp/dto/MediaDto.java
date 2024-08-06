package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.Provider.Provider;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MediaDto {

    private int id;

    private boolean type;

    private String name;

    private String originalLanguage;

    private boolean adult;

    private Date releaseDate;

    private String overview;

    private String backdropPath;

    private String posterPath;

    private String trailerLink;

    private float tmdbRating;

    private String status;

    private float totalRating;

    private int voteCount;

    private List<Provider> providers = new ArrayList<>();

    private List<String> genres = new ArrayList<>();

    private boolean watched;

    private boolean watchlist;

    private String url;

    public static MediaDto fromMedia(Media media, UriInfo uriInfo) {
        MediaDto mediaDTO = new MediaDto();
        mediaDTO.name = media.getName();
        mediaDTO.id = media.getMediaId();
        mediaDTO.voteCount = media.getVoteCount();
        mediaDTO.adult = media.isAdult();
        mediaDTO.releaseDate = media.getReleaseDate();
        mediaDTO.overview = media.getOverview();
        mediaDTO.backdropPath = media.getBackdropPath();
        mediaDTO.posterPath = media.getPosterPath();
        mediaDTO.trailerLink = media.getTrailerLink();
        mediaDTO.tmdbRating = media.getTmdbRating();
        mediaDTO.status = media.getStatus();
        mediaDTO.totalRating = media.getTotalRating();
        mediaDTO.providers.addAll(media.getProviders()); //TODO FIX THIS
        mediaDTO.watched = media.isWatched();
        mediaDTO.watchlist = media.isWatchlist();
        mediaDTO.url = uriInfo.getBaseUriBuilder().path("media/{mediaId}").build(media.getMediaId()).toString();
        return mediaDTO;
    }

    protected static void setFromMediaChild(MediaDto mediaDTO, Media media, UriInfo uriInfo) {
        mediaDTO.setName(media.getName());
        mediaDTO.setId(media.getMediaId());
        mediaDTO.setVoteCount(media.getVoteCount());
        mediaDTO.setAdult(media.isAdult());
        mediaDTO.setReleaseDate(media.getReleaseDate());
        mediaDTO.setOverview(media.getOverview());
        mediaDTO.setBackdropPath(media.getBackdropPath());
        mediaDTO.setPosterPath(media.getPosterPath());
        mediaDTO.setTrailerLink(media.getTrailerLink());
        mediaDTO.setTmdbRating(media.getTmdbRating());
        mediaDTO.setStatus(media.getStatus());
        mediaDTO.setTotalRating(media.getTotalRating());
        mediaDTO.setProviders(media.getProviders());
        mediaDTO.setWatched(media.isWatched());
        mediaDTO.setWatchlist(media.isWatchlist());
        mediaDTO.setUrl(uriInfo.getBaseUriBuilder().path("media/{mediaId}").build(media.getMediaId()).toString());
    }

    public static List<MediaDto> fromMediaList(List<Media> mediaList, UriInfo uriInfo) {
        return mediaList.stream().map(m -> fromMedia(m, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int mediaId) {
        this.id = mediaId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public float getTmdbRating() {
        return tmdbRating;
    }

    public void setTmdbRating(float tmdbRating) {
        this.tmdbRating = tmdbRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(float totalRating) {
        this.totalRating = totalRating;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        return;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public boolean isWatchlist() {
        return watchlist;
    }

    public void setWatchlist(boolean watchlist) {
        this.watchlist = watchlist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
