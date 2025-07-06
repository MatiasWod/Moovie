package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;

import javax.ws.rs.core.UriInfo;

public class UserListIdDto {
    private int moovieListId;
    private String moovieListUrl;
    private String username;
    private String userUrl;
    private String url;

    public UserListIdDto() {}

    public UserListIdDto(int mlId, String username, String url, UriInfo uriInfo) {
        this.moovieListId = mlId;
        this.username = username;
        this.url = url;

        this.moovieListUrl =  uriInfo.getBaseUriBuilder().path("lists/{moovieListId}").build(moovieListId).toString();
        this.userUrl =  uriInfo.getBaseUriBuilder().path("users/{username}").build(username).toString();
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public void setMoovieListId(int moovieListId) {
        this.moovieListId = moovieListId;
    }

    public String getMoovieListUrl() {
        return moovieListUrl;
    }

    public void setMoovieListUrl(String moovieListUrl) {
        this.moovieListUrl = moovieListUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

