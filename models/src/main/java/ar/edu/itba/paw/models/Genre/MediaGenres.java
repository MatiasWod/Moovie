package ar.edu.itba.paw.models.Genre;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mediagenres")
public class MediaGenres implements Serializable {
    @Id
    @ManyToOne
    @MapsId("mediaId")
    @JoinColumn(name = "mediaId", referencedColumnName = "mediaId")
    private Media media;

    @Id
    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genreId", referencedColumnName = "genreId")
    private Genre genre;
}
