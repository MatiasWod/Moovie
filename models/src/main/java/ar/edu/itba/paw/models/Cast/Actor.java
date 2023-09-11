package ar.edu.itba.paw.models.Cast;

public abstract class Actor {
    private final String actorName;
    private final String characterName;
    private final String profilePath;

    public Actor(String actorName, String characterName, String profilePath) {
        this.actorName = actorName;
        this.characterName = characterName;
        this.profilePath = profilePath;
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
