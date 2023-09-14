package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoovieListServiceImpl implements MoovieListService{
    @Autowired
    private MoovieListDao mediaListDao;

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return mediaListDao.getMoovieListById(moovieListId);
    }

    @Override
    public List<MoovieList> geAllMoovieLists() {
        return mediaListDao.geAllMoovieLists();
    }

    @Override
    public List<MoovieListContent> getMoovieListContentById(int moovieListId) {
        return mediaListDao.getMoovieListContentById(moovieListId);
    }
}
