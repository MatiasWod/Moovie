package ar.edu.itba.paw.models.Provider;

public class Provider {
    private final int providerId;
    private final String providerName;
    private final String logoPath;

    public Provider(int providerId, String providerName, String logoPath) {
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
