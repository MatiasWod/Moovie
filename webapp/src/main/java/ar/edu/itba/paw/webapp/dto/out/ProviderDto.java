package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Provider.Provider;

import javax.persistence.Column;

public class ProviderDto {

    private int providerId;
    private String providerName;
    private String logoPath;

    public static ProviderDto fromProvider(Provider p) {
        ProviderDto dto = new ProviderDto();
        dto.providerId = p.getProviderId();
        dto.providerName = p.getProviderName();
        dto.logoPath = p.getLogoPath();
        return dto;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
