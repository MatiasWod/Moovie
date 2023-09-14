package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MediaList.MediaList;

import java.util.Optional;

public interface MediaListDao {
    Optional<MediaList> getMediaListById(int mediaListId);
}
