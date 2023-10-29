package ar.edu.itba.paw.models.TV;

import javax.persistence.*;

@Entity
@Table(name = "creators")
public class TVCreators {

    @Id
    @Column(name = "mediaId")
    private int mediaId;

//    @Id
    @Column(name = "creatorId")
    private int creatorId;

    @Column(name = "creatorName")
    private String creatorName;

    TVCreators(){

    }

    public TVCreators(final int mediaId, final int creatorId, final String creatorName) {
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
