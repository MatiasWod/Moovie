package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.extendedMoovieList;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.CreateListForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class ListController {

    @Autowired
    private MoovieListService moovieListService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(@RequestParam(value = "search", required = false) final String searchQ) {
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        mav.addObject("showLists", moovieListService.getMoovieListCards(null, null, moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, moovieListService.DEFAULT_PAGE_SIZE_CARDS, 0));
        return mav;
    }

    private String extractNumericPart(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }



// http://tuDominio.com/createList?s=A&s=B&s=C&s=D&s=E

    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) List<String> genre,
                                   @RequestParam(value = "m", required = false) String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "s", required = false) List<String> selected,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

        return new ModelAndView();
    }

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        return new ModelAndView();
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        return new ModelAndView();
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId, @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        /*try{
            ModelAndView mav = new ModelAndView("helloworld/moovieList");
            MoovieListCard moovieListCard = moovieListService.getMoovieListCardById(moovieListId);
            List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContent(moovieListId, null, moovieListService.DEFAULT_PAGE_SIZE_CONTENT, 0);


            moovieListCard.getSize();

            return mav;
        }catch (MoovieListNotFoundException e){
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", e.getMessage());
            return mav;
        }catch(InvalidAccessToResourceException e){
            final ModelAndView mav = new ModelAndView("helloworld/404.jsp");
            mav.addObject("extraInfo", e.getMessage());
            return mav;
        }*/
        return new ModelAndView();
    }

    /*@RequestMapping(value = "/featuredList/topRated")
    public ModelAndView topRatedList(){
        List<Integer> mediaIdList = mediaService.getMediaIdOrderedByTmdbRatingDesc(25,0);
        MoovieList moovieList = moovieListService.createStandardPublicMoovieListWithContent("Top Rated","These are the best movies and series according to our users' rating",mediaIdList);
        return list(moovieList.getMoovieListId());
    }

    @RequestMapping(value = "/featuredList/mostPopular")
    public ModelAndView mostPopularList(){
        MoovieList moovieList = moovieListService.
    }*/

    @RequestMapping(value = "/createListAction", method = RequestMethod.POST)
    public ModelAndView createListAction(@Valid @ModelAttribute("ListForm") final CreateListForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createList(null, null, null, null, form);
        }

        MoovieList list = moovieListService.createMoovieListWithContent(form.getListName(), moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC , form.getListDescription(), form.getMediaIdsList());

        int id = list.getMoovieListId();
        return new ModelAndView("redirect:/list/" + id);
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ModelAndView putLike(@RequestParam("listId") int listId) {
        moovieListService.likeMoovieList(listId);
        return new ModelAndView("redirect:/list/" + listId);
    }



}
