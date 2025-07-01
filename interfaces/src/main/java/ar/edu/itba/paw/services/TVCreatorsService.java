package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;

public interface TVCreatorsService {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId, int pageNumber, int pageSize);
    int getTvCreatorsByMediaIdCount(int mediaId);
    TVCreators getTvCreatorById(int creatorId);
    List<TVCreators> getTVCreatorsForQuery(String query, int pageNumber, int pageSize);
    int getTVCreatorsForQueryCount(String query);
    List<Media> getMediasForTVCreator(int creatorId, int pageNumber, int pageSize);
    int getMediasForTVCreatorCount(int creatorId);
}
