package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface TVSerieService {
    Optional<TVSerie> getById(int tvId);

    List<TVSerie> getTvList();


    Optional<Integer> getTvCount();
}
