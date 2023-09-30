package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;

public interface MoovieListService {

    public static final int DEFAULT_PAGE_SIZE = 25;

    Optional<MoovieList> getMoovieListById(int moovieListId);
    //MoovieList getMoovieListById(int moovieListId);
    List<MoovieList> getAllMoovieLists(int size, int pageNumber);
    Optional<Integer> getMoovieListCount();
    List<MoovieListContent> getMoovieListContentById(int moovieListId);
    MoovieList createStandardPublicMoovieList( String name, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    MoovieList createStandardPublicMoovieListWithContent( String name, String description, List<Integer> mediaIdList);
    //void deleteMoovieList(int moovieIdList);

    //Returns the media id of the moovies watched in certain list;
    List<Integer> getMediaIdsWatchedInMoovieList(int moovieListId);


    List<MoovieList> getAllStandardPublicMoovieListFromUser(int userId, int size, int pageNumber);
    List<MoovieList> getMoovieListDefaultPrivateFromCurrentUser();

    Optional<Integer> getLikesCount(int moovieListId);
    List<User> getAllUsersWhoLikedMoovieList(int moovieListId);

    MoovieListLikes likeMoovieList( int moovieListId); //Will like and take like if its liked
    boolean likeMoovieListStatusForUser( int moovieListId);  //Return true if user liked the MoovieList
    MoovieListLikes removeLikeMoovieList( int moovieListId);

    List<MoovieList> likedMoovieListsForUser(int userId, int size, int pageNumber);   //Returns all moovieLists liked by user

    List<MoovieList> getMoovieListBySearch(String searchString, int size, int pageNumber);
    Optional<Integer> getMoovieListSize(int moovieListId, Boolean type);
}
