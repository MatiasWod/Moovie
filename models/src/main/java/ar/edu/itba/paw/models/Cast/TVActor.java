package ar.edu.itba.paw.models.Cast;

public class TVActor extends Actor{
    private final int tvId;

    public TVActor(int actorId, String actorName, String characterName, String profilePath, int tvId) {
        super(actorId, actorName,characterName,profilePath);
        this.tvId = tvId;
    }

    public int getTvId() {
        return tvId;
    }

}
