package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;

public interface MoovieListDao {

    public static final int MOOVIE_LIST_TYPE_STANDARD_PUBLIC = 1;   //Listas que crea un usuario y son publicas
    public static final int MOOVIE_LIST_TYPE_STANDARD_PRIVATE = 2;  //Listas que crea un usuario y puso privada
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PUBLIC = 3;   //Listas creadas automaticamente por ej: "Top 50"
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PRIVATE = 4;   //Listas creadas automaticamente por ej: "Watchlist"

    Optional<MoovieList> getMoovieListById(int moovieListId);
    List<MoovieList> getAllMoovieLists(int size, int pageNumber);
    Optional<Integer> getMoovieListCount();
    List<MoovieListContent> getMoovieListContentById(int moovieListId);

    MoovieList createMoovieList(int userId, String name, int type, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    MoovieList createMoovieListWithContent(int userId, String name, int type, String description, List<Integer> mediaIdList);

    void deleteMoovieList(int userId, int moovieIdList);        //TODO NOT IMPLEMENTED YET

    Optional<Integer> getLikesCount(int moovieListId);
    List<User> getAllUsersWhoLikedMoovieList(int moovieListId);
    MoovieListLikes likeMoovieList(int userId, int moovieListId);
    boolean likeMoovieListStatusForUser(int userId, int moovieListId);  //Return true if user liked the MoovieList
    List<MoovieList> likedMoovieListsForUser(int userId, int size, int pageNumber);   //Returns all moovieLists liked by user
    MoovieListLikes removeLikeMoovieList(int userId, int moovieListId);

    Optional<Integer> getMoovieListSize(int moovieListId, Boolean type);
}
