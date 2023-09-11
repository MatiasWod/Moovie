package ar.edu.itba.paw.models.Cast;

public class TVActor extends Actor{
    private final int tvId;

    public TVActor(int tvId, String actorName, String characterName, String profilePath) {
        super(actorName,characterName,profilePath);
        this.tvId = tvId;
    }

    public int getTvId() {
        return tvId;
    }

}
