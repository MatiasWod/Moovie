package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;

import java.util.List;
import java.util.Optional;


public interface MoovieListDao {



    Optional<MoovieList> getMoovieListById(int moovieListId);

    Optional<MoovieListCard> getMoovieListCardById(int moovieListId, int currentUserId);
    List<MoovieListCard> getLikedMoovieListCards(int userId,int type, int size, int pageNumber, int currentUserId);

    List<MoovieListCard> getMoovieListCards(String search, String ownerUsername , int type , String orderBy, String order, int size, int pageNumber, int currentUserId);
    int getMoovieListCardsCount(String search, String ownerUsername , int type , int size, int pageNumber);

    List<MoovieListContent> getMoovieListContent(int moovieListId, int userid, String orderBy,String sortOrder, int size, int pageNumber);

    List<MoovieListContent> getFeaturedMoovieListContent(int moovieListId, int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber);
    int countWatchedFeaturedMoovieListContent(int moovieListId, int mediaType, int userid , String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber);


    MoovieList createMoovieList(int userId, String name, int type, String description);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
    void deleteMediaFromMoovieList(int moovieListId, int mediaId);
    void deleteMoovieList(int moovieListId);


    void removeLikeMoovieList(int userId, int moovieListId);
    void likeMoovieList(int userId, int moovieListId);
}
