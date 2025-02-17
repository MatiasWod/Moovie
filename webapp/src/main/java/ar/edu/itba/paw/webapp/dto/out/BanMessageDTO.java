package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.BannedMessage.BannedMessage;

import javax.ws.rs.core.UriInfo;

public class BanMessageDTO {
    private int moderatorId;
    private int bannedUserId;
    private String banMessage;
    private String URL;

    public static BanMessageDTO fromBannedMessage(BannedMessage bannedMessage, String bannedUsername, UriInfo uriInfo) {
        BanMessageDTO banMessageDTO = new BanMessageDTO();
        banMessageDTO.banMessage = bannedMessage.getMessage();
        banMessageDTO.bannedUserId = bannedMessage.getBannedUserId();
        banMessageDTO.moderatorId = bannedMessage.getModUserId();
        banMessageDTO.URL = uriInfo.getBaseUriBuilder().path("/users/" + bannedUsername + "/banMessage").toString();

        return banMessageDTO;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getBannedUserId() {
        return bannedUserId;
    }

    public void setBannedUserId(int bannedUserId) {
        this.bannedUserId = bannedUserId;
    }

    public String getBanMessage() {
        return banMessage;
    }

    public void setBanMessage(String banMessage) {
        this.banMessage = banMessage;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
