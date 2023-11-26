package ar.edu.itba.paw.models.MoovieList;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Formula("SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.moovieListId = :moovieListId")
    private int totalReports;
    @Formula("SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 3 AND rr.moovieListId = :moovieListId")
    private int spamReports;
    @Formula("SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 0 AND rr.moovieListId = :moovieListId")
    private int hateReports;
    @Formula("SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 2 AND rr.moovieListId = :moovieListId")
    private int privacyReports;
    @Formula("SELECT COUNT(*) FROM reportsMoovieLists rr WHERE rr.type = 1 AND rr.moovieListId = :moovieListId")
    private int abuseReports;

//    @OneToMany(mappedBy = "moovieList")
//    final private Set<MoovieListLikes> likes = new HashSet<>();
//
//    @OneToMany(mappedBy = "user")
//    final private Set<MoovieListFollowers> followers = new HashSet<>();

    public MoovieList(){}

    public MoovieList(int userId, String name, String description, int type) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

//    public Set<MoovieListFollowers> getFollowers() {
//        return followers;
//    }
//
//    public Set<MoovieListLikes> getLikes() {
//        return likes;
//    }


    public int getTotalReports() {
        return totalReports;
    }

    public int getSpamReports() {
        return spamReports;
    }

    public int getHateReports() {
        return hateReports;
    }

    public int getPrivacyReports() {
        return privacyReports;
    }

    public int getAbuseReports() {
        return abuseReports;
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

