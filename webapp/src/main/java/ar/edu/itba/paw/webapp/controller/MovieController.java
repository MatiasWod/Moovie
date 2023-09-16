package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class MovieController {

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MoovieListService moovieListService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ReviewService reviewService;


    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        List<Movie> movieList = mediaService.getMovieList();
        mav.addObject("movieList", movieList);
        List<TVSerie> tvSerieList = mediaService.getTvList();
        mav.addObject("tvList", tvSerieList);

        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView test(@RequestParam(value = "g", required = false) String genre, @RequestParam(value = "media", required = false) String media) {
        final ModelAndView mav = new ModelAndView("helloworld/discover");

        List<Movie> movieList;
        List<TVSerie> tvSerieList;
        List<Media> mediaList;

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

        List<String> genres = genreService.getAllGenres();
        mav.addObject("genresList", genres);

        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value="query", required = true) String query){

        final ModelAndView mav = new ModelAndView("helloworld/discover");

        Boolean search = true;
        mav.addObject("searchMode",search);

        List<Media> mediaList = mediaService.getMediaBySearch(query);
        mav.addObject("mediaList", mediaList);


        return mav;
    }


    @RequestMapping("/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId) {
        final ModelAndView mav = new ModelAndView("helloworld/details");
        final Optional<Movie> media = mediaService.getMovieById(mediaId);
        final List<Actor> actorsList = actorService.getAllActorsForMedia(mediaId);
        final List<Genre> genresList = genreService.getGenreForMedia(mediaId);
        if (media.isPresent())
            mav.addObject("media", media.get());
        else
            mav.addObject("media", null);
        mav.addObject("actorsList", actorsList);
        mav.addObject("genresList", genresList);
        return mav;
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId){
        final ModelAndView mav = new ModelAndView("helloworld/moovieList");

        Optional<MoovieList> moovieListData = moovieListService.getMoovieListById(moovieListId);
        if(moovieListData.isPresent()){
            mav.addObject("moovieList", moovieListData.get());

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
            String listOwner = userService.findUserById(moovieListData.get().getUserId()).getEmail();

            mav.addObject("mediaList", mediaList);
            mav.addObject("moovieListContent", moovieListContent);
            mav.addObject("listOwner", listOwner);
        }
        else {
        }
        return mav;
    }
}
