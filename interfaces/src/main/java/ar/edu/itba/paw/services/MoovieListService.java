package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;

import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface MoovieListService {
    Optional<MoovieList> getMoovieListById(int moovieListId);
    List<MoovieList> getAllMoovieLists();
    Optional<Integer> getMoovieListCount();
    List<MoovieListContent> getMoovieListContentById(int moovieListId);
    MoovieList createMoovieList(int userId, String name, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList);
    void deleteMoovieList(int moovieIdList);

    Optional<Integer> getLikesCount(int moovieListId);
    List<User> getAllUsersWhoLikedMoovieList(int moovieListId);

    MoovieListLikes likeMoovieList(int userId, int moovieListId); //Will like and take like if its liked
    boolean likeMoovieListStatusForUser(int userId, int moovieListId);  //Return true if user liked the MoovieList
    List<MoovieList> likedMoovieListsForUser(int userId, int size, int pageNumber);   //Returns all moovieLists liked by user
    MoovieListLikes removeLikeMoovieList(int userId, int moovieListId);
}
