package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;
import java.util.Optional;

public interface TVCreatorsDao {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId, int pageNumber, int pageSize);
    int getTvCreatorsByMediaIdCount(int mediaId);
    Optional<TVCreators> getTvCreatorById(int creatorId);
    List<TVCreators> getTVCreatorsForQuery(String query, int pageNumber, int pageSize);
    int getTVCreatorsForQueryCount(String query);
    List<Media> getMediasForTVCreator(int creatorId, int pageNumber, int pageSize, int currentUser);
    int getMediasForTVCreatorCount(int creatorId);
}
