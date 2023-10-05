package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListLikes;


import javax.swing.tree.RowMapper;
import java.util.List;
import java.util.Optional;


public interface MoovieListDao {

    public static final int MOOVIE_LIST_TYPE_STANDARD_PUBLIC = 1;   //Listas que crea un usuario y son publicas
    public static final int MOOVIE_LIST_TYPE_STANDARD_PRIVATE = 2;  //Listas que crea un usuario y puso privada
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PUBLIC = 3;   //Listas creadas automaticamente por ej: "Top 50"
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PRIVATE = 4;   //Listas creadas automaticamente por ej: "Watchlist"


    Optional<MoovieList> getMoovieListById(int moovieListId);
    Optional<MoovieListCard> getMoovieListCardById(int moovieListId);
    List<MoovieListContent> getMoovieListContent(int moovieListId, int userId, String orderBy, int size, int pageNumber);
    List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , int size, int pageNumber);

    MoovieList createMoovieList(int userId, String name, int type, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMoovieList(int moovieListId);

    void likeMoovieList(int userId, int moovieListId);
    void removeLikeMoovieList(int userId, int moovieListId);
    boolean likeMoovieListStatusForUser(int userId, int moovieListId);
}
