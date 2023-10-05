package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.FailedToInsertToListException;
import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MoovieListServiceImpl implements MoovieListService{
    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public MoovieList getMoovieListById(int moovieListId) { //Check permissions
        MoovieList ml = moovieListDao.getMoovieListById(moovieListId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        User currentUser = userService.getInfoOfMyUser();
        if( ml.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE || ml.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE ){
            if(ml.getUserId() != currentUser.getUserId()){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return ml;
    }

    @Override
    public MoovieListCard getMoovieListCardById(int moovieListId) {
        MoovieListCard mlc = moovieListDao.getMoovieListCardById(moovieListId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        User currentUser = userService.getInfoOfMyUser();
        if( mlc.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE || mlc.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE ){
            if(mlc.getUsername() != currentUser.getUsername()){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return mlc;
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, int userId, String orderBy, int size, int pageNumber) {
        getMoovieListById(moovieListId);
        //If the previous didnt fail we are good to go
        return moovieListDao.getMoovieListContent(moovieListId, userId, orderBy, size, pageNumber);
    }

    @Override
    public List<MoovieListCard> getMoovieListsCards(String search, String ownerUsername, int type, int size, int pageNumber) {
        return moovieListDao.getMoovieListsCards(search, ownerUsername, type, size, pageNumber);
    }

    @Override
    public MoovieList createMoovieList(String name, int type, String description) {
        return moovieListDao.createMoovieList(userService.getInfoOfMyUser().getUserId(), name, type, description);
    }

    @Override
    public MoovieList createMoovieListWithContent(String name, int type, String description, List<Integer> mediaIdList) {
        MoovieList ml =  moovieListDao.createMoovieList(userService.getInfoOfMyUser().getUserId(), name, type, description);
        return insertMediaIntoMoovieList(ml.getMoovieListId(), mediaIdList);
    }

    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListId, List<Integer> mediaIdList) {
        MoovieList ml = getMoovieListById(moovieListId);
        User currentUser = userService.getInfoOfMyUser();
        if(ml.getUserId() == currentUser.getUserId()){
            return moovieListDao.insertMediaIntoMoovieList(moovieListId, mediaIdList);
        }
        else{
            throw new InvalidAccessToResourceException("User is not owner of the list");
        }
    }

    @Override
    public void deleteMoovieList(int moovieListId) {
        MoovieList ml = getMoovieListById(moovieListId);
        User currentUser = userService.getInfoOfMyUser();
        if(currentUser.getRole() == userService.ROLE_MODERATOR || currentUser.getUserId() == ml.getUserId()){
            deleteMoovieList(moovieListId);
        }else{
            throw new InvalidAccessToResourceException("You are not the user of this list, so you can't delete it");
        }

    }
}
