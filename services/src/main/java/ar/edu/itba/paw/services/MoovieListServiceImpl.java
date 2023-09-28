package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class MoovieListServiceImpl implements MoovieListService{
    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private UserService userService;

    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return moovieListDao.getMoovieListById(moovieListId);
    }

    @Override
    public List<MoovieList> getAllMoovieLists(int size, int pageNumber) {
        return moovieListDao.getAllMoovieLists(size, pageNumber);
    }

    @Override
    public List<MoovieListContent> getMoovieListContentById(int moovieListId) {
        return moovieListDao.getMoovieListContentById(moovieListId);
    }

    @Override
    public MoovieList createStandardPublicMoovieList(String name,  String description) {
        User u = userService.getInfoOfMyUser();
        return moovieListDao.createMoovieList(u.getUserId(), name, moovieListDao.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, description);
    }

    @Override
    public MoovieList createStandardPublicMoovieListWithContent( String name,  String description, List<Integer> mediaIdList) {
        User u = userService.getInfoOfMyUser();
        return moovieListDao.createMoovieListWithContent(u.getUserId(), name, moovieListDao.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, description, mediaIdList);
    }


    @Override
    public void deleteMoovieList(int moovieListId) {
        int uid = userService.getInfoOfMyUser().getUserId();
        MoovieList ml = getMoovieListById(moovieListId).orElseThrow(() -> new MoovieListNotFoundException("No moovie list found for id " + moovieListId) );
        if(ml.getUserId() == uid ){
            moovieListDao.deleteMoovieList( uid , moovieListId);
            return;
        }
        throw new InvalidAccessToResourceException("This list doesnt belong to user logged, so cant be deleted");
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
        if(likeMoovieListStatusForUser(userId, moovieListId)){
            return moovieListDao.removeLikeMoovieList(userId, moovieListId);
        }
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

    @Override
    public MoovieListLikes removeLikeMoovieList(int userId, int moovieListId) {
        return moovieListDao.removeLikeMoovieList(userId,moovieListId);
    }
}
