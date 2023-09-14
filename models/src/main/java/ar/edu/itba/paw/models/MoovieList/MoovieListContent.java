package ar.edu.itba.paw.models.MoovieList;

public class MoovieListContent{
    private final int mediaListId;
    private final int mediaId;
    private final String status;

    public MoovieListContent(int mediaListId, int mediaId, String status) {
        this.mediaListId = mediaListId;
        this.mediaId = mediaId;
        this.status = status;
    }

    public int getMoovieListId() {
        return mediaListId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getStatus() {
        return status;
    }
}
