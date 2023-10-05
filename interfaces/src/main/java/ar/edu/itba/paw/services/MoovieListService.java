package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;

import java.util.List;
import java.util.Optional;

public interface MoovieListService {

    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final int MOOVIE_LIST_TYPE_STANDARD_PUBLIC = 1;   //Listas que crea un usuario y son publicas
    public static final int MOOVIE_LIST_TYPE_STANDARD_PRIVATE = 2;  //Listas que crea un usuario y puso privada
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PUBLIC = 3;   //Listas creadas automaticamente por ej: "Top 50"
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PRIVATE = 4;   //Listas creadas automaticamente por ej: "Watchlist"

    //Get the moovieList object, doesnt contain  much info
    MoovieList getMoovieListById(int moovieListId);
    //Gets the moovieListCard (recomended for querys and contains useful info for the visualization of a MoovieList
    MoovieListCard getMoovieListCardById(int moovieListId);
    //Get the content of media of some moovieList by its id TODO MISSING THE BOOLEAN ISWATCHED FUNTIONALITY
    List<MoovieListContent> getMoovieListContent(int moovieListId, int userId, String orderBy, int size, int pageNumber);
    //Get the MoovieListCard, which contains the element presented in searchs, has a lot of arguments for searchs/querys
    List<MoovieListCard> getMoovieListsCards(String search, String ownerUsername , int type , int size, int pageNumber);


    //Gets all the Moovie
    List<MoovieListCard> getAllMoovieListCardFromUser(int userId, int type, int size, int pageNumber);


    //Create or insert into moovieList
    MoovieList createMoovieList(String name, int type, String description);
    MoovieList createMoovieListWithContent(String name, int type, String description, List<Integer> mediaIdList);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMoovieList(int moovieListId);

    //Likes functions
    void likeMoovieList( int moovieListId);
    void removeLikeMoovieList(int moovieListId);
    boolean likeMoovieListStatusForUser( int moovieListId);
}
