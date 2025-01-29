package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaIdListIdDto {
    private int mediaId;
    private int moovieListId;

    public MediaIdListIdDto() {
    }

    public MediaIdListIdDto(int mediaId, int moovieListId) {
        this.mediaId = mediaId;
        this.moovieListId = moovieListId;
    }

    public static List<MediaIdListIdDto> fromMediaList(List<Media> medias, int moovieListId){
        List<MediaIdListIdDto> toRet = new ArrayList<>();
        for (Media media : medias) {
            toRet.add(new MediaIdListIdDto(media.getMediaId(), moovieListId));
        }
        return toRet;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setMoovieListId(int moovieListId) {
        this.moovieListId = moovieListId;
    }
}
