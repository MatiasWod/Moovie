package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.TV.TVCreators;

import java.util.List;

public interface TVCreatorsService {
    List<TVCreators> getTvCreatorsByMediaId(int mediaId);
}
