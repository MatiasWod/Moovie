package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;

public interface TVCreatorsDao {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId);
}
