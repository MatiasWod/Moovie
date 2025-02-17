package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public class MoovieListReviewDto {

    private int id;

    private int moovieListid;

    private int reviewLikes;

    private boolean currentUserHasLiked;

    private String moovieListTitle;

    private String reviewContent;

    private String totalReportsUrl;

    private String spamReportsUrl;

    private String hateReportsUrl;

    private String abuseReportsUrl;

    private String privacyReportsUrl;

    private String url;

    private String creatorUrl;

    private String moovieListUrl;

    //TODO URL a Likes y Reports


    public static MoovieListReviewDto fromMoovieListReview(final MoovieListReview moovieListReview, UriInfo uriInfo){
        MoovieListReviewDto moovieListReviewDto= new MoovieListReviewDto();
        moovieListReviewDto.id=moovieListReview.getMoovieListReviewId();
        moovieListReviewDto.moovieListid=moovieListReview.getMoovieListId();
        moovieListReviewDto.reviewLikes=moovieListReview.getReviewLikes();
        moovieListReviewDto.currentUserHasLiked=moovieListReview.isCurrentUserHasLiked();
        moovieListReviewDto.moovieListTitle=moovieListReview.getMoovieListTitle();
        moovieListReviewDto.reviewContent= moovieListReview.getReviewContent();

        moovieListReviewDto.totalReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .build()
                .toString();

        moovieListReviewDto.spamReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.spam.getType())
                .build()
                .toString();

        moovieListReviewDto.hateReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.hatefulContent.getType())
                .build()
                .toString();

        moovieListReviewDto.privacyReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.privacy.getType())
                .build()
                .toString();

        moovieListReviewDto.abuseReportsUrl = uriInfo.getBaseUriBuilder().path("/reports/count")
                .queryParam("contentType", "moovieListReview")
                .queryParam("resourceId", moovieListReview.getMoovieListReviewId())
                .queryParam("type", ReportTypesEnum.abuse.getType())
                .build()
                .toString();




        moovieListReviewDto.url= uriInfo.getBaseUriBuilder().path("/moovieListReview/{id}").build(moovieListReview.getMoovieListReviewId()).toString();
        moovieListReviewDto.creatorUrl=uriInfo.getBaseUriBuilder().path("/users/username/{username}").build(moovieListReview.getUser().getUsername()).toString();
        moovieListReviewDto.moovieListUrl=uriInfo.getBaseUriBuilder().path("/list/{id}").build(moovieListReview.getMoovieListId()).toString();

        return moovieListReviewDto;
    }

    public static List<MoovieListReviewDto> fromMoovieListReviewList(final List<MoovieListReview> moovieListReviews,UriInfo uriInfo){
        return moovieListReviews.stream().map(r->fromMoovieListReview(r,uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getMoovieListid(){
        return moovieListid;
    }

    public void setMoovieListid(int moovieListid){
        this.moovieListid = moovieListid;
    }

    public int getReviewLikes(){
        return reviewLikes;
    }

    public void setReviewLikes(int reviewLikes){
        this.reviewLikes = reviewLikes;
    }

    public boolean isCurrentUserHasLiked(){
        return currentUserHasLiked;
    }

    public void setCurrentUserHasLiked(boolean currentUserHasLiked){
        this.currentUserHasLiked = currentUserHasLiked;
    }

    public String getMoovieListTitle(){
        return moovieListTitle;
    }

    public void setMoovieListTitle(String moovieListTitle){
        this.moovieListTitle = moovieListTitle;
    }

    public String getReviewContent(){
        return reviewContent;
    }

    public void setReviewContent(String reviewContent){
        this.reviewContent = reviewContent;
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

    public String getPrivacyReportsUrl() {
        return privacyReportsUrl;
    }

    public void setPrivacyReportsUrl(String privacyReportsUrl) {
        this.privacyReportsUrl = privacyReportsUrl;
    }

    public void setAbuseReportsUrl(String abuseReportsUrl) {
        this.abuseReportsUrl = abuseReportsUrl;
    }


    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getCreatorUrl(){
        return creatorUrl;
    }

    public void setCreatorUrl(String creatorUrl){
        this.creatorUrl = creatorUrl;
    }

    public String getMoovieListUrl(){
        return moovieListUrl;
    }

    public void setMoovieListUrl(String moovieListUrl){
        this.moovieListUrl = moovieListUrl;
    }

}
