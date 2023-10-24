package ar.edu.itba.paw.models.Provider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "providers")
public class Provider {
    @Id
    @Column(name = "providerId")
    private int providerId;

    @Column(length = 100, name = "providerName")
    private String providerName;

    @Column(length = 100, name = "logopath")
    private String logoPath;

    /* Just for Hibernate*/
    Provider(){

    }

    public Provider(final int providerId, final String providerName, final String logoPath) {
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
