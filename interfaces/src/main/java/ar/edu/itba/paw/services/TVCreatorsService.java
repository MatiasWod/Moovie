package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.Optional;

public interface TVCreatorsService {
    Optional<TVCreators> getTvCreatorByMediaId(int mediaId);
}
