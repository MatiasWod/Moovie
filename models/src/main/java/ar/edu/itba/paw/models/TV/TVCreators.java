package ar.edu.itba.paw.models.TV;

public class TVCreators {
    private final int tvId;
    private final int creatorName;
    private final String profilePath;

    public TVCreators(int tvId, int creatorName, String profilePath) {
        this.tvId = tvId;
        this.creatorName = creatorName;
        this.profilePath = profilePath;
    }

    public int getTvId() {
        return tvId;
    }

    public int getCreatorName() {
        return creatorName;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
