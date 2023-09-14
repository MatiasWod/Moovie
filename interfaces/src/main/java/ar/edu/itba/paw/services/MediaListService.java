package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MediaList.MediaList;
import ar.edu.itba.paw.models.MediaList.MediaListContent;

import java.util.Optional;

public interface MediaListService {
    Optional<MediaList> getMediaListById(int mediaListId);
    Optional<MediaListContent> getMediaListContentById(int mediaListId);
}
