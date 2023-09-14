package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MediaList.MediaListContent;
import ar.edu.itba.paw.persistence.MediaListContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MediaListContentServiceImpl implements MediaListContentService{
    @Autowired
    private MediaListContentDao mediaListContentDao;

    @Override
    public Optional<MediaListContent> getMediaListContentById(int mediaListId) {
        return mediaListContentDao.getMediaListContentById(mediaListId);
    }
}
