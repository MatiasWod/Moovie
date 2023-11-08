package ar.edu.itba.paw.models.Cast;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "actors")
public class Actor {
    @EmbeddedId
    private ActorId id;

    @Column(length = 100, name = "actorName")
    private String actorName;

    @Column(length = 100, name = "characterName")
    private String characterName;

    @Column(length = 100, name = "profilePath")
    private String profilePath;



    /* Just for Hibernate*/
    Actor() {

    }

    public Actor(final int mediaId, final int actorId, final String actorName, final String characterName, final String profilePath) {
        this.id = new ActorId(mediaId,actorId);
        this.actorName = actorName;
        this.characterName = characterName;
        this.profilePath = profilePath;
    }

    public ActorId getId() {
        return id;
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

@Embeddable
class ActorId implements Serializable{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mediaId")
    private int mediaId;

    @Column(name = "actorId")
    private int actorId;

    ActorId(){

    }

    public ActorId(final int mediaId, final int actorId){
        this.actorId = actorId;
        this.mediaId = mediaId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getActorId() {
        return actorId;
    }
}
