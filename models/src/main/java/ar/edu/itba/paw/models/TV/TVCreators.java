package ar.edu.itba.paw.models.TV;

public class TVCreators {
    private final int mediaId;
    private final int creatorId;
    private final String creatorName;
    private final String profilePath;

    public TVCreators(int mediaId, int creatorId, String creatorName, String profilePath) {
        this.mediaId = mediaId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.profilePath = profilePath;
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

    public String getProfilePath() {
        return profilePath;
    }
}
