package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.Provider.dimensionedProvider;

import java.util.List;

public interface ProviderDao {
    List<dimensionedProvider> getAllProviders();

}
