package ar.edu.itba.paw.models.Provider;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "providers")
public class Provider {
    @EmbeddedId
    private ProviderId id;

    @Column(name = "providerName", length = 100, nullable = false)
    private String providerName;

    @Column(name = "logoPath", length = 100, nullable = false)
    private String logoPath;

    /* Just for Hibernate*/
    Provider(){

    }

    public Provider(final int mediaId,final int providerId, final String providerName, final String logoPath, int mediaid) {
        this.id = new ProviderId(mediaId, providerId);
        this.providerName = providerName;
        this.logoPath = logoPath;

    }


    public ProviderId getId() {
        return id;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getLogoPath() {
        return logoPath;
    }
}

@Embeddable
class ProviderId implements Serializable {
    @Column(name = "mediaid")
    private int mediaId;

    @Column(name = "providerId")
    private int providerId;

    public ProviderId(final int mediaId, final int providerId){
        this.mediaId = mediaId;
        this.providerId = providerId;
    }

    ProviderId(){

    }

    public int getProviderId() {
        return providerId;
    }

    public int getMediaId() {
        return mediaId;
    }
}