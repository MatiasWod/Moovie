package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.Optional;

public interface TVCreatorsDao {
    Optional<TVCreators> getTvCreatorByMediaId(int mediaId);
}
