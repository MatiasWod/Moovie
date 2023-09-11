package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;

import java.util.List;
import java.util.Optional;

public interface ActorDao {
    List<Actor> getAllActorsForMedia(int mediaId);//fijarse qué hacer con tvId y movieId
}
