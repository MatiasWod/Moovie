package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.Optional;

public interface ProviderService {

    Optional<Provider> getProviderForMedia(int mediaId);
}