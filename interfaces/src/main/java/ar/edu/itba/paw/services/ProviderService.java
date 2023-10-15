package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.Provider.dimensionedProvider;

import java.util.List;

public interface ProviderService {
    List<dimensionedProvider> getAllProviders();
}