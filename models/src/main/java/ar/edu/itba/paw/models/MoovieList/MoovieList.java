package ar.edu.itba.paw.models.MoovieList;

import javax.persistence.*;

@Entity
@Table(name="moovielists")
public class MoovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moovielists_moovielistid_seq")
    @SequenceGenerator(sequenceName = "moovielists_moovielistid_seq", name = "moovielists_moovielistid_seq", allocationSize = 1)
    @Column(name = "moovielistId")
    private int moovieListId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int type;

    public MoovieList(){}

    public MoovieList(final int moovieListId, final int userId, final String name, final String description, final int type) {
        this.moovieListId = moovieListId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }
}

