package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.Provider.Provider;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
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
    public ModelAndView details(@PathVariable("id") final int mediaId,@ModelAttribute("CreateReviewForm") final CreateReviewForm createReviewForm){
        final ModelAndView mav = new ModelAndView("helloworld/details");
        final Optional<Media> media = mediaService.getMediaById(mediaId);
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

        Map<Integer, String> userEmail = new HashMap<>();
        for (Review review : reviewList) {
            userEmail.put(review.getUserId(), Objects.requireNonNull(userService.findUserById(review.getUserId()).orElse(null)).getEmail());
        }

        if (media.isPresent()) {
            if (!media.get().isType()) {
                final Optional<Movie> movie = mediaService.getMovieById(mediaId);
                mav.addObject("media", movie.orElse(null)); // Use orElse to handle empty Optional
            } else {
                final Optional<TVSerie> tvSerie = mediaService.getTvById(mediaId);
                final List<TVCreators> creators=tvCreatorsService.getTvCreatorsByMediaId(mediaId);
                if (!creators.isEmpty())
                    mav.addObject("creators", creators);
                mav.addObject("media", tvSerie.orElse(null)); // Use orElse to handle empty Optional
            }
        } else {
            mav.addObject("media", null);
        }

        mav.addObject("providerList", providerList);
        mav.addObject("actorsList", actorsList);
        mav.addObject("genresList", genresList);
        mav.addObject("notEmptyContentReviewList", notEmptyContentReviewList);
        mav.addObject("userEmail", userEmail);

        return mav;
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public String createReview( @Valid @ModelAttribute("CreateReviewForm") final CreateReviewForm createReviewForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return "redirect:/asdad/";
        }
        User user = userService.getOrCreateUserViaMail(createReviewForm.getUserEmail());
        reviewService.createReview(user.getUserId(), createReviewForm.getMediaId(), createReviewForm.getRating(), createReviewForm.getReviewContent());
        return ("redirect:/details/" + createReviewForm.getMediaId());
    }


    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId) {
        final ModelAndView mav = new ModelAndView("helloworld/moovieList");

        Optional<MoovieList> moovieListData = moovieListService.getMoovieListById(moovieListId);
        if (moovieListData.isPresent()) {
            mav.addObject("moovieList", moovieListData.get());

            List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
            String listOwner = userService.findUserById(moovieListData.get().getUserId()).get().getEmail();

            mav.addObject("mediaList", mediaList);
            mav.addObject("moovieListContent", moovieListContent);
            mav.addObject("listOwner", listOwner);
        } else {
        }
        return mav;
    }
}

