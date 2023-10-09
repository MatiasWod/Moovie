package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.services.GenreService;
import ar.edu.itba.paw.services.MediaService;
import ar.edu.itba.paw.services.MoovieListService;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(@RequestParam(value = "search", required = false) final String search,
                              @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");
        mav.addObject("showLists", moovieListService.getMoovieListCards(search, null, moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, moovieListService.DEFAULT_PAGE_SIZE_CARDS, pageNumber - 1));
        int listCount = moovieListService.getMoovieListCardsCount(search,null,moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC,moovieListService.DEFAULT_PAGE_SIZE_CARDS, pageNumber - 1).get().intValue();
        int numberOfPages = (int) Math.ceil(listCount * 1.0 / moovieListService.DEFAULT_PAGE_SIZE_CONTENT);
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
                                   @RequestParam(value = "s", required = false) List<String> selected,
                                   @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

        final ModelAndView mav = new ModelAndView("helloworld/createList");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("Movies and Series")){
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_ALL,null,genres,null, mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getTotalMediaCount(mediaService.TYPE_ALL,null,genres).get().intValue();
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_MOVIE,null,genres,null, mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getTotalMediaCount(mediaService.TYPE_MOVIE,null,genres).get().intValue();
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(mediaService.TYPE_TVSERIE,null,genres,null, mediaService.DEFAULT_PAGE_SIZE,pageNumber - 1));
            mediaCount = mediaService.getTotalMediaCount(mediaService.TYPE_TVSERIE,null,genres).get().intValue();
        }
        numberOfPages = (int) Math.ceil(mediaCount * 1.0 / mediaService.DEFAULT_PAGE_SIZE);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("genresList", genreService.getAllGenres());
        return mav;
    }

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watched",username,moovieListService.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE,mediaService.DEFAULT_PAGE_SIZE,0);
        return list(moovieListCards.get(0).getMoovieListId(),1,null,null);
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watchlist",username,moovieListService.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE,mediaService.DEFAULT_PAGE_SIZE,0);
        return list(moovieListCards.get(0).getMoovieListId(),1,null,null);
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId, @RequestParam(value = "page", defaultValue = "1") final int pageNumber, @RequestParam(value="orderBy", defaultValue = "name") final String orderBy, @RequestParam(value="order", defaultValue = "asc") final String order) {
        final ModelAndView mav = new ModelAndView("helloworld/moovieList");
        mav.addObject("moovieList",moovieListService.getMoovieListById(moovieListId));
        List<MoovieListContent> mediaList=moovieListService.getMoovieListContent(moovieListId,orderBy,order,MoovieListService.DEFAULT_PAGE_SIZE_CONTENT,pageNumber - 1);
        mav.addObject("mediaList",mediaList);
        mav.addObject("watchedCount",moovieListService.countWatchedMovies(mediaList));
        final MoovieListCard moovieListCard = moovieListService.getMoovieListCardById(moovieListId);
        int mediaCountForMoovieList = moovieListCard.getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / moovieListService.DEFAULT_PAGE_SIZE_CONTENT);
        mav.addObject("initialOrder",order);
        mav.addObject("itemsPerPage",moovieListService.DEFAULT_PAGE_SIZE_CONTENT);
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
            return createList(null, null, null, null,1, form);
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
