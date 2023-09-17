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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
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
        } else if (media != null && media.equals("Movies")) {
            movieList = mediaService.getMovieList();
            mav.addObject("mediaList", movieList);
        } else if (media != null && media.equals("Series")) {
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
    public ModelAndView search(@RequestParam(value = "query", required = true) String query) {

        final ModelAndView mav = new ModelAndView("helloworld/discover");

        Boolean search = true;
        mav.addObject("searchMode", search);

        List<Media> mediaList = mediaService.getMediaBySearch(query);
        mav.addObject("mediaList", mediaList);


        return mav;
    }


    @RequestMapping(value = "/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId) {
        final Optional<Media> media = mediaService.getMediaById(mediaId);

        if (media.isPresent()) {
            final ModelAndView mav = new ModelAndView("helloworld/details");
            final List<Actor> actorsList = actorService.getAllActorsForMedia(mediaId);
            final List<Genre> genresList = genreService.getGenreForMedia(mediaId);
            final List<Review> reviewList = reviewService.getReviewsByMediaId(mediaId);
            if (!media.get().isType()) {
                final Optional<Movie> movie = mediaService.getMovieById(mediaId);
                mav.addObject("media", movie.orElse(null)); // Use orElse to handle empty Optional
            } else {
                final Optional<TVSerie> tvSerie = mediaService.getTvById(mediaId);
                mav.addObject("media", tvSerie.orElse(null)); // Use orElse to handle empty Optional
            }
            mav.addObject("actorsList", actorsList);
            mav.addObject("genresList", genresList);
            mav.addObject("reviewList", reviewList);

            return mav;

        } else {
            return error("Theres media with id: " + String.valueOf(mediaId) );
        }
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public String createReview(@RequestParam(value = "userEmail", required = true) final String userEmail,
                               @RequestParam(value = "mediaId", required = true) final int mediaId,
                               @RequestParam(value = "rating", required = true) final int rating,
                               @RequestParam(value = "reviewContent", required = false) final String reviewContent) {
        User user = userService.getOrCreateUserViaMail(userEmail);
        if (reviewContent == null || reviewContent.isEmpty()) {
            //ratingservice
        } else
            reviewService.createReview(user.getUserId(), mediaId, rating, reviewContent);
        return ("redirect:/details/" + mediaId);
    }


    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId) {

        Optional<MoovieList> moovieListData = moovieListService.getMoovieListById(moovieListId);

        if (moovieListData.isPresent()) {
            final ModelAndView mav = new ModelAndView("helloworld/moovieList");
            mav.addObject("moovieList", moovieListData.get());

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
            String listOwner = userService.findUserById(moovieListData.get().getUserId()).get().getEmail();

            mav.addObject("mediaList", mediaList);
            mav.addObject("moovieListContent", moovieListContent);
            mav.addObject("listOwner", listOwner);
            return mav;
        } else {
            return error("Theres lists with id: " + String.valueOf(moovieListId) );
        }

    }

    @RequestMapping("error/404")
    public ModelAndView error(@RequestParam(value="extraInfo", required = false) final String extraInfo)
    {
        final ModelAndView mav = new ModelAndView("helloworld/404");
        if(extraInfo != null){
            mav.addObject("extraInfo", extraInfo);
        }

        return mav;
    }
}

