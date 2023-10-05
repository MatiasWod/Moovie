package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.MoovieList.MoovieListCard;


import javax.swing.tree.RowMapper;
import java.util.List;


public interface MoovieListDao {

    public static final int MOOVIE_LIST_TYPE_STANDARD_PUBLIC = 1;   //Listas que crea un usuario y son publicas
    public static final int MOOVIE_LIST_TYPE_STANDARD_PRIVATE = 2;  //Listas que crea un usuario y puso privada
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PUBLIC = 3;   //Listas creadas automaticamente por ej: "Top 50"
    public static final int MOOVIE_LIST_TYPE_DEFAULT_PRIVATE = 4;   //Listas creadas automaticamente por ej: "Watchlist"

    List<MoovieListCard> getMoovieListsCards(String search, String ownerUsername , int type , int size, int pageNumber);

}
