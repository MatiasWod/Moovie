package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.persistence.ActorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActorServiceImpl implements ActorService{
    @Autowired
    private ActorDao actorDao;

    @Transactional(readOnly = true)
    @Override
    public List<Actor> getAllActorsForMedia(int mediaId) {
        return actorDao.getAllActorsForMedia(mediaId);
    }
}
