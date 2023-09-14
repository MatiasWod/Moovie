package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MediaList.MediaList;
import ar.edu.itba.paw.models.MediaList.MediaListContent;
import ar.edu.itba.paw.persistence.MediaListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaListServiceImpl implements MediaListService{
    @Autowired
    private MediaListDao mediaListDao;

    @Override
    public Optional<MediaList> getMediaListById(int mediaListId) {
        return mediaListDao.getMediaListById(mediaListId);
    }

    @Override
    public List<MediaListContent> getMediaListContentById(int mediaListId) {
        return mediaListDao.getMediaListContentById(mediaListId);
    }
}
