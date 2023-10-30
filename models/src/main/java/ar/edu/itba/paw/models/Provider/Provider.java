package ar.edu.itba.paw.models.Provider;

import javax.persistence.*;

@Entity
@Table(name = "providers")
public class Provider {
    @Column(name = "providerId")
    private int providerId;

    @Column(length = 100, name = "providerName")
    private String providerName;

    @Column(length = 100, name = "logopath")
    private String logoPath;

    @Id
    @Column
    private int mediaId;

    /* Just for Hibernate*/
    Provider(){

    }

    public Provider(final int mediaId,final int providerId, final String providerName, final String logoPath) {
        this.mediaId = mediaId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.logoPath = logoPath;
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
