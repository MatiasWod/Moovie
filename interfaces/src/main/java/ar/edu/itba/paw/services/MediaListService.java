package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MediaList.MediaList;

import java.util.Optional;

public interface MediaListService {
    Optional<MediaList> getMediaListById(int mediaListId);
}
