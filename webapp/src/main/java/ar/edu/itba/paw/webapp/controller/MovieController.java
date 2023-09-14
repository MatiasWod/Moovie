package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MovieController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MoovieListService mediaListService;


    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/testdatabase");
        final List<Movie> mediaList = mediaService.getMovieFilteredByGenre("Action");
        mav.addObject("mediaList", mediaList);
        return mav;
    }


    //    discover/
    //    disover/?g={genre}

    @RequestMapping("/discover")
    public ModelAndView test(@RequestParam(value = "g", required = false) String genre, @RequestParam(value = "media", required = false) String media) {
        final ModelAndView mav = new ModelAndView("helloworld/discover");

        List<Movie> movieList;
        List<TVSerie> tvSerieList;
        List<Media> mediaList;
        List<String> genres = genreService.getAllGenres();

        if (genre != null && !genre.isEmpty()) {
            if (media != null && media.equals("Movies")) {
                movieList = mediaService.getMovieFilteredByGenre(genre);
                mav.addObject("mediaList", movieList);
            } else if (media != null && media.equals("Series")) {
                tvSerieList = mediaService.getTvFilteredByGenre(genre);
                mav.addObject("mediaList", tvSerieList);
            } else {
                mediaList = mediaService.getMediaFilteredByGenre(genre);
                mav.addObject("mediaList", mediaList);
            }
        } else if (media != null && media.equals("Movies")){
            movieList = mediaService.getMovieList();
            mav.addObject("mediaList", movieList);
        } else if (media != null && media.equals("Series")){
            tvSerieList = mediaService.getTvList();
            mav.addObject("mediaList", tvSerieList);
        } else {
            mediaList = mediaService.getMoovieList();
            mav.addObject("mediaList", mediaList);
        }



        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value="query", required = true) String query){

        final ModelAndView mav = new ModelAndView("helloworld/discover");

        Boolean search = true;
        mav.addObject("searchMode",search);

        List<Media> mediaList = mediaService.getMediaBySearch(query);
        mav.addObject("mediaList", mediaList);

        List<String> genres = genreService.getAllGenres();
        mav.addObject("genresList", genres);
        return mav;
    }


    @RequestMapping("/list")
    public ModelAndView list(){

        final ModelAndView mav = new ModelAndView("helloworld/mediaList");

        int mediaListId = 1;

        Optional<MoovieList> mediaListData = mediaListService.getMoovieListById(mediaListId);
        if(mediaListData.isPresent()){
            mav.addObject("mediaListData", mediaListData.get());
        }
        List<Media> mediaList = mediaService.getMediaByMoovieListId(mediaListId);
        List<MoovieListContent> mediaListContent = mediaListService.getMoovieListContentById(mediaListId);

        mav.addObject("mediaList", mediaList);
        mav.addObject("mediaListContent", mediaListContent);

        return mav;
    }


}
