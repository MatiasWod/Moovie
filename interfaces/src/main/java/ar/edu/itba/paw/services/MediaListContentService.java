package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MediaList.MediaListContent;

import java.util.Optional;

public interface MediaListContentService {
    Optional<MediaListContent> getMediaListContentById(int mediaListId);
}
