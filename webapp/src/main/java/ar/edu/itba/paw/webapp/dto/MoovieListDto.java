package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.Review.MoovieListReview;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class MoovieListDto {

    private int id;

    private int userId;

    private String name;

    private String description;

    //TODO Considerar que capaz no hay que devolver esto
    private int type;

    private int likes;

    private int followers;

    private int mediaCount;

    private List<MoovieListReview> reviews;

    private List<String> images;

    private String url;

    private String contentUrl;

    public static MoovieListDto fromMoovieList(MoovieListCard moovieList, UriInfo uriInfo) {
        MoovieListDto dto = new MoovieListDto();
        dto.name = moovieList.getName();
        dto.id = moovieList.getMoovieListId();
        dto.userId = moovieList.getUserId();
        dto.name = moovieList.getName();
        dto.description = moovieList.getDescription();
        dto.type = moovieList.getType();
        dto.likes = moovieList.getLikeCount();
        dto.followers = moovieList.getFollowerCount();
        dto.mediaCount =  moovieList.getMoviesAmount();
        dto.images =  moovieList.getImages();
        dto.url = uriInfo.getBaseUriBuilder().path("list/{mediaId}").build(moovieList.getMoovieListId()).toString();
        dto.contentUrl = uriInfo.getBaseUriBuilder().path("list/{mediaId}/content").build(moovieList.getMoovieListId()).toString();
        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getMediaCount() {
        return mediaCount;
    }

    public void setMediaCount(int mediaCount) {
        this.mediaCount = mediaCount;
    }

    public List<MoovieListReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MoovieListReview> reviews) {
        this.reviews = reviews;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
