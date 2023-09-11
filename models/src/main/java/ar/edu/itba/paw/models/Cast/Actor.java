package ar.edu.itba.paw.models.Cast;

public class Actor {
    private final int mediaId;
    private final int actorId;
    private final String actorName;
    private final String characterName;
    private final String profilePath;

    public Actor(int mediaId, int actorId, String actorName, String characterName, String profilePath) {
        this.mediaId = mediaId;
        this.actorId = actorId;
        this.actorName = actorName;
        this.characterName = characterName;
        this.profilePath = profilePath;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getActorId() {
        return actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getProfilePath() {
        return profilePath;
    }
}
