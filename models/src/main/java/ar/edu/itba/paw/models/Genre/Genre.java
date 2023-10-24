package ar.edu.itba.paw.models.Genre;

import javax.persistence.*;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mediaid")
    private Integer id;

    @Column(name = "genre", length = 100, nullable = false)
    private String genre;

    /* Just for Hibernate*/
    Genre(){

    }

    public Genre( final String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }
}
