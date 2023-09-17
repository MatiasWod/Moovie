package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.List;
import java.util.Optional;

public interface ProviderService {

    List<Provider> getProviderForMedia(int mediaId);
}