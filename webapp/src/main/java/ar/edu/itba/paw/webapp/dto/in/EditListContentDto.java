package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;

public class EditListContentDto {
    @NotNull
    private int mediaId;
    @NotNull
    private int customOrder;

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getCustomOrder() {
        return customOrder;
    }

    public void setCustomOrder(int customOrder) {
        this.customOrder = customOrder;
    }
}
