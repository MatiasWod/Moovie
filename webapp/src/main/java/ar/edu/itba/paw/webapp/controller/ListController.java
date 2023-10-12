package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ListController {

    @Autowired
    private MoovieListService moovieListService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(@RequestParam(value = "search", required = false) final String search,
                              @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        mav.addObject("showLists", moovieListService.getMoovieListCards(search, null, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber - 1));
        int listCount = moovieListService.getMoovieListCardsCount(search,null,MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber - 1);
        int numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        final Map<String, String> queries = new HashMap<>();
        queries.put("search", search);
        String urlBase = UriComponentsBuilder.newInstance().path("/lists").query("search={search}").buildAndExpand(queries).toUriString();
        mav.addObject("urlBase", urlBase);
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
    public ModelAndView createList(@RequestParam(value = "g", required = false) List<String> genres,
                                   @RequestParam(value = "m", required = false,defaultValue = "Movies and Series") String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

        final ModelAndView mav = new ModelAndView("helloworld/createList");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("Movies and Series")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query,genres, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query,genres);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), query, genres, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_MOVIE.getType(), query,genres);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), query, genres,  null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_TVSERIE.getType(), query,genres);
        }
        numberOfPages = (int) Math.ceil(mediaCount * 1.0 / PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("genresList", genreService.getAllGenres());
        return mav;
    }

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watched",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        return list(moovieListCards.get(0).getMoovieListId(),1,null,null);
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watchlist",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        return list(moovieListCards.get(0).getMoovieListId(),1,null,null);
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId, @RequestParam(value = "page", defaultValue = "1") final int pageNumber, @RequestParam(value="orderBy", defaultValue = "name") final String orderBy, @RequestParam(value="order", defaultValue = "asc") final String order) {
        final ModelAndView mav = new ModelAndView("helloworld/moovieList");
        int pagesSize= PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        MoovieListDetails myList=moovieListService.getMoovieListDetails(moovieListId,null,null,orderBy,order,pagesSize,pageNumber - 1);
        final MoovieListCard moovieListCard = myList.getCard();
        int mediaCountForMoovieList =moovieListCard.getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);

        mav.addObject("moovieList",moovieListCard);
        mav.addObject("mediaList",myList.getContent());
        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("watchedCount",moovieListService.countWatchedMoviesInList(currentUser.getUserId(),moovieListId));
        }catch (Exception e){
            mav.addObject("watchedCount",0);
        }
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("initialOrder",order);
        mav.addObject("itemsPerPage",pagesSize);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("isLiked",moovieListService.likeMoovieListStatusForUser(moovieListId));
        mav.addObject("likedCount",moovieListCard.getLikeCount());
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        return mav;
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
            return createList(null, null, null,1, form);
        }

        MoovieList list = moovieListService.createMoovieListWithContent(form.getListName(), MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType() , form.getListDescription(), form.getMediaIdsList());

        int id = list.getMoovieListId();
        return new ModelAndView("redirect:/list/" + id);
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ModelAndView putLike(@RequestParam("listId") int listId) {
        moovieListService.likeMoovieList(listId);
        return new ModelAndView("redirect:/list/" + listId);
    }
}
