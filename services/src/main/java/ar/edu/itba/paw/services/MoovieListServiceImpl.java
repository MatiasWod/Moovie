package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
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
    private MoovieListDao moovieListDao;

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return moovieListDao.getMoovieListById(moovieListId);
    }

    @Override
    public List<MoovieList> geAllMoovieLists() {
        return moovieListDao.geAllMoovieLists();
    }

    @Override
    public List<MoovieListContent> getMoovieListContentById(int moovieListId) {
        return moovieListDao.getMoovieListContentById(moovieListId);
    }

    @Override
    public MoovieList createMoovieList(int userId, String name, String description) {
        return moovieListDao.createMoovieList(userId, name, description);
    }

    @Override
    public MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList) {
        return moovieListDao.createMoovieListWithContent(userId, name, description, mediaIdList);
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        return moovieListDao.insertMediaIntoMoovieList(moovieListid, mediaIdList);
    }

    @Override
    public Optional<Integer> getMoovieListCount() {
        return moovieListDao.getMoovieListCount();
    }


    @Override
    public Optional<Integer> getLikesCount(int moovieListId) {
        return moovieListDao.getLikesCount(moovieListId);
    }

    @Override
    public List<User> getAllUsersWhoLikedMoovieList(int moovieListId) {
        return moovieListDao.getAllUsersWhoLikedMoovieList(moovieListId);
    }

    @Override
    public MoovieListLikes likeMoovieList(int userId, int moovieListId) {
        return moovieListDao.likeMoovieList(userId, moovieListId);
    }

    @Override
    public boolean likeMoovieListStatusForUser(int userId, int moovieListId) {
        return moovieListDao.likeMoovieListStatusForUser(userId, moovieListId);
    }

    @Override
    public List<MoovieList> likedMoovieListsForUser(int userId, int size, int pageNumber) {
        return moovieListDao.likedMoovieListsForUser(userId, size, pageNumber);
    }
}
