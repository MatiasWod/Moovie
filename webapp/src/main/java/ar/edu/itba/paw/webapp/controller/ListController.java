package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.MoovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ListController {

    @Autowired
    private MoovieListService mediaListService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MediaService mediaService;

    @RequestMapping("/lists")
    public ModelAndView lists(){
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        List<MoovieList> moovieLists = mediaListService.geAllMoovieLists();
        mav.addObject("moovieLists", moovieLists);
        return mav;
    }

// http://tuDominio.com/createList?s=A&s=B&s=C&s=D&s=E
    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) String genre,
                                   @RequestParam(value = "m", required = false) String media,
                                   @RequestParam(value = "q", required = false) String query){

        List<Movie> movieList = null;
        List<TVSerie> tvSerieList = null;
        List<Media> mediaList = null;
        List<Media> searchList;

        final ModelAndView mav = new ModelAndView("helloworld/createList");

        if (genre != null && !genre.isEmpty()) {
            if (media != null && media.equals("Movies")) {
                movieList = mediaService.getMovieFilteredByGenre(genre);
//                mav.addObject("mediaList", movieList);
            } else if (media != null && media.equals("Series")) {
                tvSerieList = mediaService.getTvFilteredByGenre(genre);
//                mav.addObject("mediaList", tvSerieList);
            } else {
                mediaList = mediaService.getMediaFilteredByGenre(genre);
//                mav.addObject("mediaList", mediaList);
            }
        } else if (media != null && media.equals("Movies")){
            movieList = mediaService.getMovieList();
//            mav.addObject("mediaList", movieList);
        } else if (media != null && media.equals("Series")){
            tvSerieList = mediaService.getTvList();
//            mav.addObject("mediaList", tvSerieList);
        } else {
            mediaList = mediaService.getMoovieList();
//            mav.addObject("mediaList", mediaList);
        }

        if( query != null && !query.isEmpty()){
            searchList = mediaService.getMediaBySearch(query);
            if (movieList != null){
                List<Integer> mediaIdList = movieList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));
            } else if (tvSerieList != null) {
                List<Integer> mediaIdList = tvSerieList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));
            } else if (mediaList != null) {
                List<Integer> mediaIdList = mediaList.stream()
                        .map(Media::getMediaId)
                        .collect(Collectors.toList());

                searchList.removeIf(aux -> !mediaIdList.contains(aux.getMediaId()));

            }
            mav.addObject("mediaList",searchList);
        }else{
            if (movieList != null){
                mav.addObject("mediaList",movieList);
            } else if (tvSerieList != null) {
                mav.addObject("mediaList",tvSerieList);
            } else if (mediaList != null) {
                mav.addObject("mediaList",mediaList);
            }
        }

        List<String> genres = genreService.getAllGenres();
        mav.addObject("genresList", genres);
        return mav;
    }
}
