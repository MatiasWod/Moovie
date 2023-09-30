package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

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

    @Autowired
    private ProviderService providerService;

    @Autowired
    private TVCreatorsService tvCreatorsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        List<Movie> movieList = mediaService.getMovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("movieList", movieList);
        List<TVSerie> tvSerieList = mediaService.getTvList(mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("tvList", tvSerieList);

        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView test(@RequestParam(value = "g", required = false) List<String> genres, @RequestParam(value = "media", required = false) String media) {
        final ModelAndView mav = new ModelAndView("helloworld/discover");




        List<Movie> movieList;
        List<TVSerie> tvSerieList;
        List<Media> mediaList;

        if (genres != null && !genres.isEmpty()) {
            if (media != null && media.equals("Movies")) {
                movieList = mediaService.getMovieFilteredByGenreList(genres, mediaService.DEFAULT_PAGE_SIZE, 0);
                movieList.forEach(movie -> {
                    if (movie.getOverview().contains("\n")){
                        movie.setOverview(movie.getOverview().replace("\n",""));
                    }
                });
                mav.addObject("mediaList", movieList);
            } else if (media != null && media.equals("Series")) {
                tvSerieList = mediaService.getTvFilteredByGenreList(genres, mediaService.DEFAULT_PAGE_SIZE, 0);
                tvSerieList.forEach(mediaAux -> {
                    if (mediaAux.getOverview().contains("\n")){
                        mediaAux.setOverview(mediaAux.getOverview().replace("\n",""));
                    }
                });
                mav.addObject("mediaList", tvSerieList);
            } else {
                mediaList = mediaService.getMediaFilteredByGenreList(genres, mediaService.DEFAULT_PAGE_SIZE, 0);
                mediaList.forEach(mediaAux -> {
                    if (mediaAux.getOverview().contains("\n")){
                        mediaAux.setOverview(mediaAux.getOverview().replace("\n",""));
                    }
                });
                mav.addObject("mediaList", mediaList);
            }
        } else if (media != null && media.equals("Movies")) {
            movieList = mediaService.getMovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
            movieList.forEach(movie -> {
                if (movie.getOverview().contains("\n")){
                    movie.setOverview(movie.getOverview().replace("\n",""));
                }
            });
            mav.addObject("mediaList", movieList);
        } else if (media != null && media.equals("Series")) {
            tvSerieList = mediaService.getTvList(mediaService.DEFAULT_PAGE_SIZE, 0);
            tvSerieList.forEach(mediaAux -> {
                if (mediaAux.getOverview().contains("\n")){
                    mediaAux.setOverview(mediaAux.getOverview().replace("\n",""));
                }
            });
            mav.addObject("mediaList", tvSerieList);
        } else {
            mediaList = mediaService.getMoovieList(mediaService.DEFAULT_PAGE_SIZE, 0);
            mediaList.forEach(mediaAux -> {
                if (mediaAux.getOverview().contains("\n")){
                    mediaAux.setOverview(mediaAux.getOverview().replace("\n",""));
                }
            });
            mav.addObject("mediaList", mediaList);
        }

        List<String> genresList = genreService.getAllGenres();
        mav.addObject("genresList", genresList);

        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query", required = true) String query) {

        final ModelAndView mav = new ModelAndView("helloworld/discover");

        Boolean search = true;
        mav.addObject("searchMode", search);

        List<Media> mediaList = mediaService.getMediaBySearch(query, mediaService.DEFAULT_PAGE_SIZE, 0);

        mediaList.forEach(media -> {
            if (media.getOverview().contains("\n")){
                media.setOverview(media.getOverview().replace("\n",""));
            }
        });

        mav.addObject("mediaList", mediaList);


        return mav;
    }

    @RequestMapping(value = "/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId, @ModelAttribute("detailsForm") final CreateReviewForm form) {

        final Optional<Media> media = mediaService.getMediaById(mediaId);

        if(! media.isPresent()){
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", "The media with id: " +mediaId+ " doesn't exists");
            return mav;
        }

        final ModelAndView mav = new ModelAndView("helloworld/details");
        final List<Actor> actorsList = actorService.getAllActorsForMedia(mediaId);
        final List<Genre> genresList = genreService.getGenreForMedia(mediaId);
        final List<Review> reviewList = reviewService.getReviewsByMediaId(mediaId);
        final List<Review> notEmptyContentReviewList = new ArrayList<>();
        for (Review review : reviewList) {
            if (review.getReviewContent() != null && !review.getReviewContent().isEmpty()) {
                notEmptyContentReviewList.add(review);
            }
        }


        final List<Provider> providerList=providerService.getProviderForMedia(mediaId);

        Map<Integer, String> username = new HashMap<>();
        for (Review review : reviewList) {
            username.put(review.getUserId(), Objects.requireNonNull(userService.findUserById(review.getUserId()).orElse(null)).getUsername());
        }

        if (!media.get().isType()) {
            final Optional<Movie> movie = mediaService.getMovieById(mediaId);
            mav.addObject("media", movie.orElse(null)); // Use orElse to handle empty Optional
        } else {
            final Optional<TVSerie> tvSerie = mediaService.getTvById(mediaId);
            final List<TVCreators> creators = tvCreatorsService.getTvCreatorsByMediaId(mediaId);
            if (!creators.isEmpty())
                mav.addObject("creators", creators);
            mav.addObject("media", tvSerie.orElse(null)); // Use orElse to handle empty Optional
        }

        mav.addObject("providerList", providerList);
        mav.addObject("actorsList", actorsList);
        mav.addObject("genresList", genresList);
        mav.addObject("notEmptyContentReviewList", notEmptyContentReviewList);
        mav.addObject("username", username);

        return mav;
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("detailsForm") final CreateReviewForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return details(form.getMediaId(), form);
        }
        final int userId = userService.getInfoOfMyUser().getUserId();
        reviewService.createReview(form.getMediaId(), form.getRating(), form.getReviewContent());
        return new ModelAndView("redirect:/details/" + form.getMediaId());
    }

    @ModelAttribute("user")
    public User getLoggedUser(){
        try{
            userService.getInfoOfMyUser();
        }
        catch (UnableToFindUserException exception){
            return null;
        }
        return userService.getInfoOfMyUser();
    }

}

