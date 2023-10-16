package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.persistence.MoovieListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MoovieListServiceImpl implements MoovieListService{
    @Autowired
    private MoovieListDao moovieListDao;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private static final int EVERY_THIS_AMOUNT_OF_LIKES_SEND_EMAIL = 5;

    private static final int EVERY_THIS_AMOUNT_OF_FOLLOWS_SEND_EMAIL = 5;

    @Transactional(readOnly = true)
    @Override
    public MoovieList getMoovieListById(int moovieListId) { //Check permissions
        MoovieList ml = moovieListDao.getMoovieListById(moovieListId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        if( ml.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || ml.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType() ){
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

    @Transactional(readOnly = true)
    @Override
    public MoovieListCard getMoovieListCardById(int moovieListId) {
        int currentUserId = userService.tryToGetCurrentUserId();
        MoovieListCard mlc = moovieListDao.getMoovieListCardById(moovieListId, currentUserId).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found"));
        if( mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || mlc.getType() == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
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

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListContent> getMoovieListContent(int moovieListId, String orderBy,String sortOrder, int size, int pageNumber) {
        MoovieList ml = getMoovieListById(moovieListId);
        //If the previous didnt throw exception, we have the permissions needed to perform the next action
        try{
            int userid = userService.getInfoOfMyUser().getUserId();
            return moovieListDao.getMoovieListContent(moovieListId, userid , orderBy,sortOrder ,size, pageNumber);
        } catch(UserNotLoggedException e){
            return moovieListDao.getMoovieListContent(moovieListId, -1 , orderBy,sortOrder ,size, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        //If the previous didnt throw exception, we have the permissions needed to perform the next action
        try{
            int userid = userService.getInfoOfMyUser().getUserId();
            return moovieListDao.getFeaturedMoovieListContent(moovieListId,mediaType, userid ,featuredListOrder, orderBy,sortOrder ,size, pageNumber);
        } catch(UserNotLoggedException e){
            return moovieListDao.getFeaturedMoovieListContent(moovieListId,mediaType, -1 , featuredListOrder, orderBy,sortOrder ,size, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public int countWatchedFeaturedMoovieListContent(int moovieListId, int mediaType, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        try{
            int userid = userService.getInfoOfMyUser().getUserId();
            return moovieListDao.countWatchedFeaturedMoovieListContent(moovieListId,mediaType, userid ,featuredListOrder, orderBy,sortOrder ,size, pageNumber);
        } catch(UserNotLoggedException e){
            return moovieListDao.countWatchedFeaturedMoovieListContent(moovieListId,mediaType, -1 , featuredListOrder, orderBy,sortOrder ,size, pageNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber) {
        if(type == MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType() || type == MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType()){
            if(!userService.getInfoOfMyUser().getUsername().equals(ownerUsername)){
                throw new InvalidAccessToResourceException("Need to be owner to acces thr private list of this user");
            }
        }
        return moovieListDao.getMoovieListCards(search, ownerUsername, type,orderBy,order, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername , int type , int size, int pageNumber){
        return moovieListDao.getMoovieListCardsCount(search,ownerUsername,type,size,pageNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId,int type, int size, int pageNumber){
        return moovieListDao.getLikedMoovieListCards(userId, type, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber){
        return moovieListDao.getFollowedMoovieListCards(userId, type, size, pageNumber, userService.tryToGetCurrentUserId());
    }

    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        return moovieListDao.getFollowedMoovieListCardsCount(userId,type);
    }


    @Transactional(readOnly = true)
    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber){
        List<MoovieListCard> mlc =  moovieListDao.getRecommendedMoovieListCards(moovieListId, size, pageNumber, userService.tryToGetCurrentUserId());
        if(mlc.size()<size){
            // 4 are searched in order to be 100% sure there wont be repeating elements
            List<MoovieListCard> aux =  moovieListDao.getMoovieListCards(null, null, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), " random() "," asc ", size, pageNumber, userService.tryToGetCurrentUserId());
            // A check is needed so as no to add duplicates
            boolean flag;
            for(MoovieListCard mlcAux : aux ){
                flag = true;
                for(MoovieListCard mlcOriginal : mlc){
                    if(mlcOriginal.getMoovieListId() == mlcAux.getMoovieListId()){
                        flag = false;
                    }
                }
                if(flag){
                    mlc.add(mlcAux);
                    if(mlc.size()==size){
                        return mlc;
                    }
                }
            }
        }
        return mlc;
    }


    @Transactional(readOnly = true)
    @Override
    public MoovieListDetails getMoovieListDetails(int moovieListId, String name, String ownerUsername, String orderBy, String sortOrder, int size, int pageNumber) {
        MoovieListCard card = null;
        List<MoovieListContent> content = null;

        int currentUserId = userService.tryToGetCurrentUserId();

        if(moovieListId == -1){
            List<MoovieListCard> cards = moovieListDao.getMoovieListCards(name,ownerUsername, MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), null, null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0 , currentUserId);
            if(cards.size() != 1){
                throw new MoovieListNotFoundException("MoovieList: " + name+ " of: " +ownerUsername+ " not found");
            }
             card = cards.get(0);
             content = getMoovieListContent(card.getMoovieListId(),orderBy,sortOrder,size,pageNumber);
        }
        else{
            card = moovieListDao.getMoovieListCardById(moovieListId, currentUserId).get();
            content = getMoovieListContent(moovieListId,orderBy,sortOrder,size,pageNumber);
        }
        return new MoovieListDetails(card,content);

    }

    @Transactional
    @Override
    public MoovieList createMoovieListWithContent(String name, int type, String description, List<Integer> mediaIdList) {
        MoovieList ml =  moovieListDao.createMoovieList(userService.getInfoOfMyUser().getUserId(), name, type, description);
        return insertMediaIntoMoovieList(ml.getMoovieListId(), mediaIdList);
    }

    @Transactional
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

    @Transactional
    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId) {
        MoovieList ml = getMoovieListById(moovieListId);
        User currentUser = userService.getInfoOfMyUser();
        if(ml.getUserId() == currentUser.getUserId()){
            moovieListDao.deleteMediaFromMoovieList(moovieListId, mediaId);
        }
        else{
            throw new InvalidAccessToResourceException("User is not owner of the list");
        }
    }

    @Transactional
    @Override
    public void deleteMoovieList(int moovieListId) {
        MoovieList ml = getMoovieListById(moovieListId);
        User currentUser = userService.getInfoOfMyUser();
        if(currentUser.getRole() == UserRoles.MODERATOR.getRole() || currentUser.getUserId() == ml.getUserId()){
            deleteMoovieList(moovieListId);
        }else{
            throw new InvalidAccessToResourceException("You are not the user of this list, so you can't delete it");
        }
    }

    @Transactional
    @Override
    public void likeMoovieList(int moovieListId) {
        int userId = userService.getInfoOfMyUser().getUserId();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);
        if(mlc.isCurrentUserHasLiked()){
            moovieListDao.removeLikeMoovieList(userId, moovieListId);
        } else {
            moovieListDao.likeMoovieList(userId, moovieListId);
            int likeCountForMoovieList = mlc.getLikeCount();
            if(likeCountForMoovieList != 0 && (likeCountForMoovieList % EVERY_THIS_AMOUNT_OF_LIKES_SEND_EMAIL) == 0){
                MoovieList mvlAux = getMoovieListById(moovieListId);
                User toUser = userService.findUserById(mvlAux.getUserId());
                Map<String,Object> map = new HashMap<>();
                map.put("username",toUser.getUsername());
                map.put("likes", likeCountForMoovieList);
                map.put("moovieListId",mvlAux.getMoovieListId());
                map.put("moovieListName",mvlAux.getName());
                emailService.sendEmail(toUser.getEmail(),
                        "New like goal on your list!",
                        "notificationLikeMilestoneMoovieList.html",
                        map);
            }
        }
    }


    @Transactional(readOnly = true)
    @Override
    public void removeLikeMoovieList(int moovieListId) {
        moovieListDao.removeLikeMoovieList(userService.getInfoOfMyUser().getUserId(), moovieListId);
    }

    @Transactional
    @Override
    public void followMoovieList(int moovieListId) {
        int userId = userService.tryToGetCurrentUserId();
        MoovieListCard mlc = getMoovieListCardById(moovieListId);
        if(mlc.isCurrentUserHasFollowed()){
            moovieListDao.removeFollowMoovieList(userId, moovieListId);
        } else {
            moovieListDao.followMoovieList(userId, moovieListId);
            int followCountForMoovieList = mlc.getFollowerCount();
            if(followCountForMoovieList != 0 && (followCountForMoovieList % EVERY_THIS_AMOUNT_OF_FOLLOWS_SEND_EMAIL) == 0){
                MoovieList mvlAux = getMoovieListById(moovieListId);
                User toUser = userService.findUserById(mvlAux.getUserId());
                Map<String,Object> map = new HashMap<>();
                map.put("username",toUser.getUsername());
                map.put("follows", followCountForMoovieList);
                map.put("moovieListId",mvlAux.getMoovieListId());
                map.put("moovieListName",mvlAux.getName());
                emailService.sendEmail(toUser.getEmail(),
                        "New follow goal on your list!",
                        "notificationFollowMilestoneMoovieList.html",
                        map);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void removeFollowMoovieList(int moovieListId) {
        moovieListDao.removeFollowMoovieList(userService.tryToGetCurrentUserId(), moovieListId);
    }
}
