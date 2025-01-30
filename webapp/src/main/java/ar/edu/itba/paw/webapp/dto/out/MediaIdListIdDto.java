package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.OrderedMedia;

import java.util.ArrayList;
import java.util.List;

public class MediaIdListIdDto {
    private int mediaId;
    private int moovieListId;
    private int customOrder;

    public MediaIdListIdDto() {
    }

    public MediaIdListIdDto(int mediaId, int moovieListId, int customOrder) {
        this.mediaId = mediaId;
        this.moovieListId = moovieListId;
        this.customOrder = customOrder;
    }

    public static List<MediaIdListIdDto> fromOrderedMediaList(List<OrderedMedia> medias, int moovieListId){
        List<MediaIdListIdDto> toRet = new ArrayList<>();
        for (OrderedMedia media : medias) {
            toRet.add(new MediaIdListIdDto(media.getMedia().getMediaId(), moovieListId, media.getCustomOrder()));
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

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }
}
