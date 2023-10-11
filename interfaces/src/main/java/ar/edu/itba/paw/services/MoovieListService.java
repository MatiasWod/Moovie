package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;

import java.util.List;
import java.util.Optional;

public interface MoovieListService {


    //Get the moovieList object, doesnt contain  much info
    //For controllers is best to use the CARDS
    MoovieList getMoovieListById(int moovieListId);

    MoovieList getWatchedByUserId(int userId);

    MoovieList getWatchlistByUserId(int userId);

    //Gets the moovieListCard (recomended for querys and contains useful info for the visualization of a MoovieList
    MoovieListCard getMoovieListCardById(int moovieListId);

    //Get the content of media of some moovieList by its id
    //The isWatched is returned as false (in every element) if the user who makes the query is not the owner
    List<MoovieListContent> getMoovieListContent(int moovieListId, String orderBy,String sortOrder, int size, int pageNumber);

    //Get the MoovieListCard, which contains the element presented in searchs, has a lot of arguments for searchs/querys
    List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , int size, int pageNumber);

    int getMoovieListCardsCount(String search, String ownerUsername , int type , int size, int pageNumber);

    List<MoovieListCard> getLikedMoovieListCards(int userId,int type, int size, int pageNumber);

    MoovieListDetails getMoovieListDetails(int moovieListId, String name, String ownerUsername, String orderBy, String sortOrder, int size, int pageNumber);


    //Create or insert into moovieList
    MoovieList createMoovieList(String name, int type, String description);
    MoovieList createMoovieListWithContent(String name, int type, String description, List<Integer> mediaIdList);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMoovieList(int moovieListId);

    //Likes functions
    void likeMoovieList( int moovieListId);
    void removeLikeMoovieList(int moovieListId);
    boolean likeMoovieListStatusForUser( int moovieListId);

    //Gives the amount of movies watched for a user in a moovielists
    int countWatchedMovies(List<MoovieListContent> mediaList);
}
