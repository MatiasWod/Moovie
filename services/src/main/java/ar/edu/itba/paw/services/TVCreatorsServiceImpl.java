package ar.edu.itba.paw.services;


import ar.edu.itba.paw.exceptions.TVCreatorNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
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

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId, int pageNumber, int pageSize) {
        return tvCreatorsDao.getTvCreatorsByMediaId(mediaId, pageNumber, pageSize);
    }

    @Override
    public int getTvCreatorsByMediaIdCount(int mediaId) {
        return tvCreatorsDao.getTvCreatorsByMediaIdCount(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public TVCreators getTvCreatorById(int creatorId) {
        TVCreators toReturn = tvCreatorsDao.getTvCreatorById(creatorId).orElseThrow( () -> new TVCreatorNotFoundException("TVCreator was not found for the id: " + creatorId));

        int currentUserId = userService.tryToGetCurrentUserId();

        if ( currentUserId >= 0 ){
            for(Media m : toReturn.getMedias()){
                m.setWatched(mediaService.getWatchedStatus(m.getMediaId(), currentUserId));
                m.setWatchlist(mediaService.getWatchlistStatus(m.getMediaId(),currentUserId));
            }
        }

        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Media> getMediasForTVCreator(int creatorId, int pageNumber, int pageSize) {
        return tvCreatorsDao.getMediasForTVCreator(creatorId, pageNumber, pageSize, userService.tryToGetCurrentUserId());
    }

    @Override
    public int getMediasForTVCreatorCount(int creatorId) {
        return tvCreatorsDao.getMediasForTVCreatorCount(creatorId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int pageNumber, int pageSize) {
        List<TVCreators> toReturn = tvCreatorsDao.getTVCreatorsForQuery(query, pageNumber, pageSize);

        int currentUserId = userService.tryToGetCurrentUserId();

        if(currentUserId >= 0){
            for( TVCreators tv : toReturn ){
                for(Media m : tv.getMedias()){
                    m.setWatched(mediaService.getWatchedStatus(m.getMediaId(), currentUserId));
                    m.setWatchlist(mediaService.getWatchlistStatus(m.getMediaId(),currentUserId));
                }
            }
        }


        return toReturn;
    }

    @Override
    public int getTVCreatorsForQueryCount(String query) {
        return tvCreatorsDao.getTVCreatorsForQueryCount(query);
    }


}
