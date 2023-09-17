package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Provider.Provider;

import java.util.List;
import java.util.Optional;

public interface ProviderDao {

    List<Provider> getProviderForMedia(int mediaId);
}
