package ar.edu.itba.paw.services;

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
}
