package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.MoovieListFollowers;
import ar.edu.itba.paw.models.User.User;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface MoovieListDao {
    Optional<MoovieList> getMoovieListById(int moovieListId);
    List<MoovieList> geAllMoovieLists();
    MoovieList createMoovieList(int userId, String name, String description);
    Optional<Integer> getMoovieListCount();

    MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList);

    List<MoovieListContent> getMoovieListContentById(int moovieListId);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);

    /*Optional<Integer> getFollowersCount(int moovieListId);
    List<MoovieListFollowers> getAllFollowers(int moovieListId);*/
}
