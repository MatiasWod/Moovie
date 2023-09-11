package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.TV.TVSerie;

import java.util.List;
import java.util.Optional;

public interface TVSerieDao {
    Optional<TVSerie> getById(int tvId);

    List<TVSerie> getTvList();


    Optional<Integer> getTvCount();
}
