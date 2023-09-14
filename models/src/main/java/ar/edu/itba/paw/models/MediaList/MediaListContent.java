package ar.edu.itba.paw.models.MediaList;

public class MediaListContent{
    private final int mediaListId;
    private final int mediaId;
    private final String status;

    public MediaListContent(int mediaListId, int mediaId, String status) {
        this.mediaListId = mediaListId;
        this.mediaId = mediaId;
        this.status = status;
    }

    public int getMediaListId() {
        return mediaListId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getStatus() {
        return status;
    }
}
