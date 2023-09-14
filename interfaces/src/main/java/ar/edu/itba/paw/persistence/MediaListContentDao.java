package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MediaList.MediaListContent;

import java.util.Optional;

public interface MediaListContentDao {
    Optional<MediaListContent> getMediaListContentById(int mediaListId);
}
