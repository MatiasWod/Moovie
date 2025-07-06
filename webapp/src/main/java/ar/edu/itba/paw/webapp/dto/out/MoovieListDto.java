package ar.edu.itba.paw.webapp.dto.out;

import java.util.List;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.MoovieListReview;

public class MoovieListDto {

    private int id;

    private String name;

    private String createdBy;

    private String description;

    private int type;

    private int likes;

    private int followers;

    private int mediaCount;

    private int movieCount;

    private List<MoovieListReview> reviews;

    private List<String> images;

    private String url;

    private String contentUrl;

    private String creatorUrl;

    private String totalReportsUrl;

    private String spamReportsUrl;

    private String hateReportsUrl;

    private String abuseReportsUrl;

    private String privacyReportsUrl;

    private String recommendedListsUrl;

    private String likesUrl;

    private String followersUrl;

    private String reviewsUrl;

    public static MoovieListDto fromMoovieList(MoovieListCard moovieList, UriInfo uriInfo) {
        MoovieListDto dto = new MoovieListDto();
        dto.name = moovieList.getName();
        dto.createdBy = moovieList.getUsername();
        dto.id = moovieList.getMoovieListId();
        dto.name = moovieList.getName();
        dto.description = moovieList.getDescription();
        dto.type = moovieList.getType();
        dto.likes = moovieList.getLikeCount();
        dto.followers = moovieList.getFollowerCount();
        dto.mediaCount = moovieList.getSize();
        dto.movieCount = moovieList.getMoviesAmount();
        dto.images = moovieList.getImages();
        dto.url = uriInfo.getBaseUriBuilder().path("lists/{moovieListId}").build(moovieList.getMoovieListId())
                .toString();
        dto.contentUrl = uriInfo.getBaseUriBuilder().path("lists/{moovieListId}/content")
                .build(moovieList.getMoovieListId()).toString();
        dto.creatorUrl = uriInfo.getBaseUriBuilder().path("users/{username}").build(moovieList.getUsername())
                .toString();
        dto.recommendedListsUrl = uriInfo.getBaseUriBuilder().path("lists/{moovieListId}/recommendedLists")
                .queryParam("id", moovieList.getMoovieListId()).build(moovieList.getMoovieListId()).toString();
        dto.reviewsUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews")
                .queryParam("listId", moovieList.getMoovieListId()).build().toString();

        dto.totalReportsUrl = uriInfo.getBaseUriBuilder().path("/listReports")
                .queryParam("moovieListId", moovieList.getMoovieListId())
                .build()
                .toString();

        dto.spamReportsUrl = uriInfo.getBaseUriBuilder().path("/listReports")
                .queryParam("moovieListId", moovieList.getMoovieListId())
                .queryParam("reportType", ReportTypesEnum.SPAM.getType())
                .build()
                .toString();

        dto.hateReportsUrl = uriInfo.getBaseUriBuilder().path("/listReports")
                .queryParam("moovieListId", moovieList.getMoovieListId())
                .queryParam("reportType", ReportTypesEnum.HATEFUL_CONTENT.getType())
                .build()
                .toString();

        dto.privacyReportsUrl = uriInfo.getBaseUriBuilder().path("/listReports")
                .queryParam("moovieListId", moovieList.getMoovieListId())
                .queryParam("reportType", ReportTypesEnum.PRIVACY.getType())
                .build()
                .toString();

        dto.abuseReportsUrl = uriInfo.getBaseUriBuilder().path("/listReports")
                .queryParam("moovieListId", moovieList.getMoovieListId())
                .queryParam("reportType", ReportTypesEnum.ABUSE.getType())
                .build()
                .toString();

        dto.likesUrl = uriInfo.getBaseUriBuilder().path("/lists/{listId}/likes")
                .build(moovieList.getMoovieListId())
                .toString();

        dto.followersUrl = uriInfo.getBaseUriBuilder().path("/lists/{listId}/followers")
                .build(moovieList.getMoovieListId())
                .toString();

        return dto;
    }

    public static List<MoovieListDto> fromMoovieListList(List<MoovieListCard> mlcList, UriInfo uriInfo) {
        return mlcList.stream().map(mlc -> fromMoovieList(mlc, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatorUrl() {
        return creatorUrl;
    }

    public void setCreatorUrl(String creatorUrl) {
        this.creatorUrl = creatorUrl;
    }

    public String getTotalReportsUrl() {
        return totalReportsUrl;
    }

    public void setTotalReportsUrl(String totalReportsUrl) {
        this.totalReportsUrl = totalReportsUrl;
    }

    public String getSpamReportsUrl() {
        return spamReportsUrl;
    }

    public void setSpamReportsUrl(String spamReportsUrl) {
        this.spamReportsUrl = spamReportsUrl;
    }

    public String getHateReportsUrl() {
        return hateReportsUrl;
    }

    public void setHateReportsUrl(String hateReportsUrl) {
        this.hateReportsUrl = hateReportsUrl;
    }

    public String getAbuseReportsUrl() {
        return abuseReportsUrl;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public void setLikesUrl(String likesUrl) {
        this.likesUrl = likesUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getPrivacyReportsUrl() {
        return privacyReportsUrl;
    }

    public void setPrivacyReportsUrl(String privacyReportsUrl) {
        this.privacyReportsUrl = privacyReportsUrl;
    }

    public void setAbuseReportsUrl(String abuseReportsUrl) {
        this.abuseReportsUrl = abuseReportsUrl;
    }

    public String getRecommendedListsUrl() {
        return recommendedListsUrl;
    }

    public void setRecommendedListsUrl(String recommendedListsUrl) {
        this.recommendedListsUrl = recommendedListsUrl;
    }

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }

}
