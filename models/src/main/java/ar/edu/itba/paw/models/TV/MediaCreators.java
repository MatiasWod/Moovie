package ar.edu.itba.paw.models.TV;

import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mediacreators")
public class MediaCreators implements Serializable {
    @Id
    @ManyToOne
    @MapsId("mediaId")
    @JoinColumn(name = "mediaId", referencedColumnName = "mediaId")
    private Media media;

    @Id
    @ManyToOne
    @MapsId("creatorId")
    @JoinColumn(name = "creatorId", referencedColumnName = "creatorId")
    private TVCreators tvCreators;
}
