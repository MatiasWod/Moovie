package ar.edu.itba.paw.models.Provider;

public class dimensionedProvider extends Provider{
    private final int appearances;
    public dimensionedProvider(int providerId, String providerName, String logoPath, int appearances) {
        super(providerId, providerName, logoPath);
        this.appearances = appearances;
    }

    public int getAppearances() {
        return appearances;
    }
}
