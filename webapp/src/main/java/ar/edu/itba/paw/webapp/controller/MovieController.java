package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.FailedToInsertToListException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class MovieController {

    @Autowired
    private MediaService mediaService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);



    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("helloworld/index");
        List<Media> movieList = mediaService.getMedia(mediaService.TYPE_MOVIE, null, null, null, mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("movieList", movieList);
        List<Media> tvSerieList = mediaService.getMedia(mediaService.TYPE_TVSERIE, null, null, null, mediaService.DEFAULT_PAGE_SIZE, 0);
        mav.addObject("tvList", tvSerieList);

        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView discover(@RequestParam(value = "media", required = false) String media, @RequestParam(value = "g", required = false) List<String> genres,
                             @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        return new ModelAndView();
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query", required = true) String query) {

        return new ModelAndView();
    }

    @RequestMapping(value = "/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId, @ModelAttribute("detailsForm") final CreateReviewForm form, RedirectAttributes redirectAttributes) {

        return new ModelAndView();
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("detailsForm") final CreateReviewForm form, final BindingResult errors, RedirectAttributes redirectAttributes) {
        return new ModelAndView();
    }

    @RequestMapping(value = "/insertMediaToList", method = RequestMethod.POST)
    public ModelAndView insertMediaToList(@RequestParam("listId") int listId, @RequestParam("mediaId") int mediaId, RedirectAttributes redirectAttributes) {
        return new ModelAndView();
    }


}

