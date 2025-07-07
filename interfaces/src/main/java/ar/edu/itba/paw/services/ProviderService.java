package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.List;

public interface ProviderService {
    List<Provider> getAllProviders(int pageNumber,int pageSize);
    int getAllProvidersCount();
    List<Provider> getProvidersForMedia(final int mediaId,int pageNumber,int pageSize);
    int getProvidersForMediaCount(final int mediaId);
    Provider getProviderById(final int id);

}