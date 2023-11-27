package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ActorNotFoundException;
import ar.edu.itba.paw.exceptions.TVCreatorNotFoundException;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.persistence.TVCreatorsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TVCreatorsServiceImpl implements TVCreatorsService{
    @Autowired
    private TVCreatorsDao tvCreatorsDao;

    @Transactional(readOnly = true)
    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return tvCreatorsDao.getTvCreatorsByMediaId(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public TVCreators getTvCreatorById(int creatorId) {
        return tvCreatorsDao.getTvCreatorById(creatorId).orElseThrow( () -> new TVCreatorNotFoundException("TVCreator was not found for the id: " + creatorId));
    }

    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int size) {
        return tvCreatorsDao.getTVCreatorsForQuery(query, size);
    }


}
