package ar.edu.itba.paw.services;

import ar.edu.itba.paw.persistence.DatabaseSchemaModifierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseModifierServiceImpl implements DatabaseModifierService{
    @Autowired
    DatabaseSchemaModifierDao dsmDao;

    @Override
    @Transactional
    public void updateGenres() {
        dsmDao.updateGenres();
    }

    @Override
    @Transactional
    public void updateProviders() {
        dsmDao.updateProviders();
    }

    @Override
    @Transactional
    public void updateActors() {
        dsmDao.updateActors();
    }

    @Override
    @Transactional
    public void updateCreators() {
        dsmDao.updateCreators();
    }
}
