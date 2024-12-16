package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;

public class MediaIdDto {
    private int mediaId;
    private String username;

    public MediaIdDto() {}

    public MediaIdDto(int mediaId, String username) {
        this.mediaId = mediaId;
        this.username = username;
    }

    public MediaIdDto fromUserMedia(Media obj, String username){
        MediaIdDto dto = new MediaIdDto();
        dto.mediaId = obj.getMediaId();
        dto.username = username;
        return dto;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

