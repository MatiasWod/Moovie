package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.BannedMessage.BannedMessage;

import javax.ws.rs.core.UriInfo;

public class BanMessageDTO {
    private String banMessage;
    private String url;
    private String userUrl;

    public static BanMessageDTO fromBannedMessage(BannedMessage bannedMessage, String bannedUsername, UriInfo uriInfo) {
        BanMessageDTO banMessageDTO = new BanMessageDTO();
        banMessageDTO.banMessage = bannedMessage.getMessage();
        banMessageDTO.url = uriInfo.getBaseUriBuilder().path("/users/" + bannedUsername + "/banMessage").toString();
        banMessageDTO.userUrl = uriInfo.getBaseUriBuilder().path("/users/" + bannedUsername).toString();

        return banMessageDTO;
    }

    public String getBanMessage() {
        return banMessage;
    }

    public void setBanMessage(String banMessage) {
        this.banMessage = banMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
