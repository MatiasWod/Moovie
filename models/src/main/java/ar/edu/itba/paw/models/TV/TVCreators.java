package ar.edu.itba.paw.models.TV;

public class TVCreators {
    private final int mediaId;
    private final int creatorId;
    private final String creatorName;

    public TVCreators(int mediaId, int creatorId, String creatorName) {
        this.mediaId = mediaId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

}
