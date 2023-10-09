package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
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
        if( ml.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE || ml.getType() == MOOVIE_LIST_TYPE_DEFAULT_PRIVATE ){
            try{
                User currentUser = userService.getInfoOfMyUser();
                if(ml.getUserId() != currentUser.getUserId()){
                    throw new InvalidAccessToResourceException("User is not owner of the list and its private");
                }
            } catch (UserNotLoggedException e){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return ml;
    }

    @Override
    public MoovieList getWatchedByUserId(int userId) {
        return null;
    }

    @Override
    public MoovieList getWatchlistByUserId(int userId) {
        return null;
    }

    @Override
    public MoovieListCard getMoovieListCardById(int moovieListId) {
        MoovieListCard mlc = moovieListDao.getMoovieListCardById(moovieListId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        if( mlc.getType() == MOOVIE_LIST_TYPE_STANDARD_PRIVATE || mlc.getType() == MOOVIE_LIST_TYPE_DEFAULT_PRIVATE ){
            try {
                User currentUser = userService.getInfoOfMyUser();
                if(!mlc.getUsername().equals(currentUser.getUsername()) ){
                    throw new InvalidAccessToResourceException("User is not owner of the list and its private");
                }
            }catch (UserNotLoggedException e){
                throw new InvalidAccessToResourceException("User is not owner of the list and its private");
            }
        }
        return mlc;
    }

    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, String orderBy,String sortOrder, int size, int pageNumber) {
        MoovieList ml = getMoovieListById(moovieListId);
        //If the previous didnt throw exception, we have the permissions needed to perform the next action
        List<MoovieListContent> mlc = moovieListDao.getMoovieListContent(moovieListId, orderBy,sortOrder ,size, pageNumber);
        try{
            if(ml.getUserId() != userService.getInfoOfMyUser().getUserId() ){
                for(MoovieListContent m : mlc){
                    m.setWatched(false);
                }
            }
        }catch (UserNotLoggedException e){
            for(MoovieListContent m : mlc){
                m.setWatched(false);
            }
        }

        
        return mlc;
    }

    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, int size, int pageNumber) {
        if(type == MOOVIE_LIST_TYPE_STANDARD_PRIVATE || type == MOOVIE_LIST_TYPE_DEFAULT_PRIVATE){
            if(!userService.getInfoOfMyUser().getUsername().equals(ownerUsername)){
                throw new InvalidAccessToResourceException("Need to be owner to acces thr private list of this user");
            }
        }
        return moovieListDao.getMoovieListCards(search, ownerUsername, type, size, pageNumber);
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername , int type , int size, int pageNumber){
        return moovieListDao.getMoovieListCardsCount(search,ownerUsername,type,size,pageNumber);
    }

    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId,int type, int size, int pageNumber){
        return moovieListDao.getLikedMoovieListCards(userId, type, size, pageNumber);
    }

//TODO: MANEJO DE EXCEPCIONES EN getMoovieListDetails por el Optional<>.get()
    @Override
    public MoovieListDetails getMoovieListDetails(int moovieListId, String orderBy, int size, int pageNumber) {
        MoovieListCard card = moovieListDao.getMoovieListCardById(moovieListId).get();
        List<MoovieListContent> content = moovieListDao.getMoovieListContent(moovieListId,orderBy,"asc",size,pageNumber);
        return new MoovieListDetails(card,content);
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


    @Override
    public void likeMoovieList(int moovieListId) {
        int userId = userService.getInfoOfMyUser().getUserId();
        if(likeMoovieListStatusForUser(moovieListId)){
            moovieListDao.removeLikeMoovieList(userId, moovieListId);
        }
        MoovieList mvlAux =  getMoovieListById(moovieListId);

        //Send mail
        User aux = userService.findUserById( mvlAux.getUserId());
        final Map<String,Object> mailMap = new HashMap<>();
        mailMap.put("username",aux.getUsername());
        mailMap.put("moovieListName",mvlAux.getName());
        emailService.sendEmail(aux.getEmail(), "Someone liked your list: " + mvlAux.getName() + "!!!" , "notificationLikeMoovieList.html", mailMap );


        moovieListDao.likeMoovieList(userId, moovieListId);
    }

    @Override
    public void removeLikeMoovieList(int moovieListId) {
        moovieListDao.removeLikeMoovieList(userService.getInfoOfMyUser().getUserId(), moovieListId);
    }

    @Override
    public boolean likeMoovieListStatusForUser(int moovieListId) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            return moovieListDao.likeMoovieListStatusForUser(currentUser.getUserId(), moovieListId);
        }catch (UserNotLoggedException e){
            return false;
        }
    }

    @Override
    public int countWatchedMovies(List<MoovieListContent> mediaList) {
        int watchedCount = 0;

        for (MoovieListContent media : mediaList) {
            if (media.isWatched()) {
                watchedCount++;
            }
        }

        return watchedCount;
    }
}
