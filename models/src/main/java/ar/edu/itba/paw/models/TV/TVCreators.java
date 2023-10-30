package ar.edu.itba.paw.models.TV;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "creators")
public class TVCreators {

    @EmbeddedId
    private TVCreatorId id;

    @Column(name = "creatorName")
    private String creatorName;

    TVCreators(){

    }

    public TVCreators(final int mediaId, final int creatorId, final String creatorName) {
        this.id = new TVCreatorId(mediaId,creatorId);
        this.creatorName = creatorName;
    }

    public TVCreatorId getId() {
        return id;
    }

    public String getCreatorName() {
        return creatorName;
    }

}

@Embeddable
class TVCreatorId implements Serializable {

    @Column(name = "mediaId")
    private int mediaId;


    @Column(name = "creatorId")
    private int creatorId;

    TVCreatorId(){

    }

    public TVCreatorId(final int mediaId, final int creatorId){
        this.mediaId = mediaId;
        this.creatorId = creatorId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public int getCreatorId() {
        return creatorId;
    }
}
