package ar.edu.itba.paw.models.Cast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "actors")
public class Actor {
    @Id
    @Column(name = "mediaId")
    private int mediaId;

    // @Id
    @Column(name = "actorId")
    private int actorId;

    @Column(length = 100, name = "actorName")
    private String actorName;

    @Column(length = 100, name = "characterName")
    private String characterName;

    @Column(length = 100, name = "profilePath")
    private String profilePath;

    /* Just for Hibernate*/
    Actor() {

    }

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
