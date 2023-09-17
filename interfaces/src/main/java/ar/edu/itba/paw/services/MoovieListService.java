package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;

import java.util.List;
import java.util.Optional;

public interface MoovieListService {
    Optional<MoovieList> getMoovieListById(int moovieListId);
    List<MoovieList> geAllMoovieLists();
    MoovieList createMoovieList(int userId, String name, String description);
    Optional<Integer> getMoovieListCount();

    MoovieList createMoovieListWithContent(int userId, String name, String description, List<Integer> mediaIdList);

    List<MoovieListContent> getMoovieListContentById(int moovieListId);
    MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList);
}
