package ar.edu.itba.paw.models.MoovieList;

public class MoovieListContent{
    private final int moovieListId;
    private final int mediaId;
    private final String status;

    public MoovieListContent(int moovieListId, int mediaId, String status) {
        this.moovieListId = moovieListId;
        this.mediaId = mediaId;
        this.status = status;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getStatus() {
        return status;
    }
}
