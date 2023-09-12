package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Provider;
import ar.edu.itba.paw.persistence.ProviderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService{
    @Autowired
    private ProviderDao providerDao;

    @Override
    public Optional<Provider> getProviderForMedia(int mediaId) {
        return providerDao.getProviderForMedia(mediaId);
    }
}
