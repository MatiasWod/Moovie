package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.persistence.TVCreatorsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TVCreatorsServiceImpl implements TVCreatorsService{
    @Autowired
    private TVCreatorsDao tvCreatorsDao;

    @Override
    public Optional<TVCreators> getTvCreatorByMediaId(int mediaId) {
        return tvCreatorsDao.getTvCreatorByMediaId(mediaId);
    }
}
