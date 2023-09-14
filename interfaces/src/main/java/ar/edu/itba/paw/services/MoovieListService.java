package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;

import java.util.List;
import java.util.Optional;

public interface MoovieListService {
    Optional<MoovieList> getMoovieListById(int moovieListId);
    List<MoovieList> geAllMoovieLists();
    List<MoovieListContent> getMoovieListContentById(int moovieListId);
}
