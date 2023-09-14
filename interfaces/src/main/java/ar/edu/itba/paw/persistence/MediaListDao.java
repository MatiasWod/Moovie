package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MediaList.MediaList;
import ar.edu.itba.paw.models.MediaList.MediaListContent;

import java.util.List;
import java.util.Optional;

public interface MediaListDao {
    Optional<MediaList> getMediaListById(int mediaListId);
    List<MediaListContent> getMediaListContentById(int mediaListId);
}
