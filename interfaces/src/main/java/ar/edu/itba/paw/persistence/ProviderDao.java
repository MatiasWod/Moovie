package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider;

import java.util.Optional;

public interface ProviderDao {

    Optional<Provider> getProviderForMedia(int mediaId);
}
