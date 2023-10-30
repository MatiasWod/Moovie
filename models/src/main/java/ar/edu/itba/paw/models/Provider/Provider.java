package ar.edu.itba.paw.models.Provider;

import javax.persistence.*;

@Entity
@Table(name = "providers")
public class Provider {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "providerId")
    private Integer providerId;

    @Column(name = "providerName", length = 100, nullable = false)
    private String providerName;

    @Column(name = "logoPath", length = 100, nullable = false)
    private String logoPath;

    @Column(name = "mediaid")
    private int mediaid;

    /* Just for Hibernate*/
    Provider(){

    }

    public Provider(final int mediaId,final int providerId, final String providerName, final String logoPath, int mediaid) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.logoPath = logoPath;
        this.mediaid = mediaid;
    }


    public int getProviderId() {
        return providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getLogoPath() {
        return logoPath;
    }
}
