package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MoovieListService moovieListService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private TVCreatorsService tvCreatorsService;

    @Autowired
    private ProviderService providerService;


    private static final Logger LOGGER = LoggerFactory.getLogger(MediaController.class);



    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        List<Media> movieList = mediaService.getMedia(mediaService.TYPE_MOVIE, null, null , null, null, null, "tmdbrating DESC", mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("movieList", movieList);
        List<Media> tvSerieList = mediaService.getMedia(mediaService.TYPE_TVSERIE, null, null, null , null, null,"tmdbrating DESC", mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("tvList", tvSerieList);
        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView discover(@RequestParam(value = "media", required = false, defaultValue = "Movies and Series") String media,
                                 @RequestParam(value = "g", required = false) List<String> genres,
                                 @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        final ModelAndView mav = new ModelAndView("helloworld/discover");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("Movies and Series")){
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_ALL,null, genres,null, null , null, null,  mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getMediaCount(mediaService.TYPE_ALL,null,genres,null,null,null);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_MOVIE,null,genres,null, null , null, null,  mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getMediaCount(mediaService.TYPE_MOVIE,null,genres,null,null,null);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_TVSERIE,null,genres,null, null , null, null, mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getMediaCount(mediaService.TYPE_TVSERIE,null,genres,null,null,null);
        }
        numberOfPages = (int) Math.ceil(mediaCount * 1.0 / mediaService.DEFAULT_PAGE_SIZE);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("genresList", genreService.getAllGenres());
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query", required = true) String query,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        final ModelAndView mav = new ModelAndView("helloworld/discover");
        mav.addObject("searchMode", true);
        int mediaCount = 0;
        if(type == null){
            mav.addObject("mediaList", mediaService.getMedia(mediaService.TYPE_ALL, query, null, null , null, null, null, mediaService.DEFAULT_PAGE_SIZE, pageNumber - 1));
            mediaCount = mediaService.getMediaCount(mediaService.TYPE_ALL,query,null, null,null,null);
        }else{
            if(type.equals("director")){
                mav.addObject("mediaList", mediaService.getMedia(mediaService.TYPE_ALL,null,null, null,query,null, null, mediaService.DEFAULT_PAGE_SIZE, pageNumber - 1));
                mediaCount = mediaService.getMediaCount(mediaService.TYPE_ALL,null,null, null,query,null);
            }else if(type.equals("creator")){
                mav.addObject("mediaList", mediaService.getMedia(mediaService.TYPE_ALL, null, null, null , null, query, null, mediaService.DEFAULT_PAGE_SIZE, pageNumber - 1));
                mediaCount = mediaService.getMediaCount(mediaService.TYPE_ALL, null, null, null , null, query);

            }else if(type.equals("actor")){
                mav.addObject("mediaList", mediaService.getMedia(mediaService.TYPE_ALL, null, null, query , null, null, null, mediaService.DEFAULT_PAGE_SIZE, pageNumber - 1));
                mediaCount = mediaService.getMediaCount(mediaService.TYPE_ALL, null, null, query , null, null);
            }
        }

        int numberOfPages = (int) Math.ceil(mediaCount * 1.0 / mediaService.DEFAULT_PAGE_SIZE);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        return mav;

    }

    @RequestMapping(value = "/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId, @ModelAttribute("detailsForm") final CreateReviewForm form, RedirectAttributes redirectAttributes) {
        boolean type;
        try{
            type = mediaService.getMediaById(mediaId).isType();
        } catch (MediaNotFoundException e){
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", e.getMessage());
            return mav;
        }
        
        final ModelAndView mav = new ModelAndView("helloworld/details");
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
        if (errorMessage != null) {
            // Add the error message to the ModelAndView
            mav.addObject("errorMessage", errorMessage);
        }
        String successMessage = (String) redirectAttributes.getFlashAttributes().get("successMessage");
        if (successMessage != null) {
            // Add the error message to the ModelAndView
            mav.addObject("successMessage", successMessage);
        }
        try{
            String username =  userService.getInfoOfMyUser().getUsername();
            mav.addObject("publicLists", moovieListService.getMoovieListCards(null, username, moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, moovieListService.DEFAULT_PAGE_SIZE_CARDS, 0));
            mav.addObject("privateLists", moovieListService.getMoovieListCards(null, username, moovieListService.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE, moovieListService.DEFAULT_PAGE_SIZE_CARDS, 0));
        }catch(Exception e){
        }
        if(!type){
            mav.addObject("media",mediaService.getMovieById(mediaId));
        } else{
            mav.addObject("media", mediaService.getTvById(mediaId));
            mav.addObject("creators", tvCreatorsService.getTvCreatorsByMediaId(mediaId));
        }
        mav.addObject("actorsList", actorService.getAllActorsForMedia(mediaId));
        mav.addObject("reviewsList", reviewService.getReviewsByMediaId(mediaId));
        mav.addObject("providerList", providerService.getProviderForMedia(mediaId));
        mav.addObject("genresList", genreService.getGenresForMedia(mediaId));
        return mav;
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("detailsForm") final CreateReviewForm form, final BindingResult errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return details(form.getMediaId(), form,null);
        }
        try{
            reviewService.createReview(form.getMediaId(), form.getRating(), form.getReviewContent());
            redirectAttributes.addFlashAttribute("successMessage", "Review has been successfully created.");

        } catch(UnableToInsertIntoDatabase e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Review wasn't created, user already has a review for this media.");
        }

        return new ModelAndView("redirect:/details/" + form.getMediaId());
    }

    @RequestMapping(value = "/insertMediaToList", method = RequestMethod.POST)
    public ModelAndView insertMediaToList(@RequestParam("listId") int listId, @RequestParam("mediaId") int mediaId, RedirectAttributes redirectAttributes) {
        try {
            moovieListService.insertMediaIntoMoovieList(listId, Collections.singletonList(mediaId));
            redirectAttributes.addFlashAttribute("successMessage", "Media has been successfully added to your list.");
        } catch (UnableToInsertIntoDatabase exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to insert media into the list. Already in the list.");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }
}

