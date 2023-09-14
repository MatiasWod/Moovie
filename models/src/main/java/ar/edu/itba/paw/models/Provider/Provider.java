package ar.edu.itba.paw.models.Provider;

public class Provider {
    private final int mediaId;
    private final int providerId;
    private final String providerName;
    private final String logoPath;

    public Provider(int mediaId, int providerId, String providerName, String logoPath) {
        this.mediaId = mediaId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.logoPath = logoPath;
    }

    public int getMediaId() {
        return mediaId;
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
