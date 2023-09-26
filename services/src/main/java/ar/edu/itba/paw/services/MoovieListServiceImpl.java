package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListFollowers;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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

    @Override
    public MoovieList createMoovieList(int userId, String name, String description) {
        return mediaListDao.createMoovieList(userId, name, description);
    }

    @Override
    public MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList) {
        return mediaListDao.createMoovieListWithContent(userId, name, description, mediaIdList);
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        return mediaListDao.insertMediaIntoMoovieList(moovieListid, mediaIdList);
    }

    @Override
    public Optional<Integer> getMoovieListCount() {
        return mediaListDao.getMoovieListCount();
    }

    /*@Override
    public Optional<Integer> getFollowersCount(int moovieListId) {
        return mediaListDao.getFollowersCount(moovieListId);
    }

    @Override
    public List<MoovieListFollowers> getAllFollowers(int moovieListId) {
        return mediaListDao.getAllFollowers(moovieListId);
    }*/
}
