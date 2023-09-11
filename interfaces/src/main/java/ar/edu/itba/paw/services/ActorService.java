package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Cast.Actor;

import java.util.List;

public interface ActorService {
    List<Actor> getAllActorsForMedia(int mediaId);//fijarse qué hacer con tvId y movieId

}
