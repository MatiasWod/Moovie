package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CreateListForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
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

    @Autowired
    private EmailService emailService;

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
                                   @RequestParam(value="orderBy", defaultValue = "tmdbRating") final String orderBy,
                                   @RequestParam(value="order", defaultValue = "desc") final String order,
                                   @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

        final ModelAndView mav = new ModelAndView("helloworld/createList");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("Movies and Series")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query, null, genres, null, orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query,genres);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), query, null,  genres, null, orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_MOVIE.getType(), query,genres);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), query, null,  genres,  null,  orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
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
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1);
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watchlist",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1);
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId,
                             @RequestParam(value="orderBy", defaultValue = "name") final String orderBy,
                             @RequestParam(value="order", defaultValue = "asc") final String order,
                             @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
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
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),1,0).get(0).getMoovieListId());
        }catch (Exception e){
            mav.addObject("watchedCount",0);
        }
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("isLiked",moovieListService.likeMoovieListStatusForUser(moovieListId));
        mav.addObject("likedCount",moovieListCard.getLikeCount());
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        Integer id = moovieListId;
        final Map<String, String> queries = new HashMap<>();
        queries.put("id",id.toString());
        queries.put("orderBy", orderBy);
        queries.put("order", order);
        String urlBase = UriComponentsBuilder.newInstance().path("/list/{id}").query("orderBy={orderBy}&order={order}").buildAndExpand(queries).toUriString();
        mav.addObject("urlBase", urlBase);
        return mav;
    }

    @RequestMapping(value = "/featuredList/{list}")
    public ModelAndView featuredList(
                                     @PathVariable("list") final String list,
                                     @RequestParam(value="orderBy", defaultValue = "tmdbRating") final String orderBy,
                                     @RequestParam(value="order", defaultValue = "desc") final String order,
                                     @RequestParam(value = "page",defaultValue = "1") final int pageNumber){
        final ModelAndView mav = new ModelAndView("helloworld/moovieList");
        int pagesSize= PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        MoovieListCard moovieListCard;
        List<MoovieListContent> moovieListContentList;
        if(list.equals("topRatedMovies")){
            moovieListCard = moovieListService.getMoovieListCards("Top Rated Movies","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_MOVIE.getType(), "tmdbrating" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else if (list.equals("topRatedSeries")){
            moovieListCard = moovieListService.getMoovieListCards("Top Rated TV Series","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_TVSERIE.getType(), "tmdbrating" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else if (list.equals("topRatedMedia")){
            moovieListCard = moovieListService.getMoovieListCards("Top Rated Media","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_ALL.getType(), "tmdbrating" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else if (list.equals("mostPopularMovies")){
            moovieListCard = moovieListService.getMoovieListCards("Most Popular Movies","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_ALL.getType(), "votecount" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else if (list.equals("mostPopularSeries")){
            moovieListCard = moovieListService.getMoovieListCards("Most Popular Series","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_ALL.getType(), "votecount" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else if (list.equals("mostPopularMedia")){
            moovieListCard = moovieListService.getMoovieListCards("Most Popular Media","Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_ALL.getType(), "votecount" ,orderBy,
                    order,pagesSize,pageNumber - 1);
        }
        else {
            return new ModelAndView("helloworld/404");
        }
        int mediaCountForMoovieList = PagingSizes.FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT.getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);
        mav.addObject("moovieList",moovieListCard);
        mav.addObject("mediaList",moovieListContentList);
        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("watchedCount",moovieListService.countWatchedMoviesInList(currentUser.getUserId(),moovieListCard.getMoovieListId()));
        }catch (Exception e){
            mav.addObject("watchedCount",0);
        }
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("isLiked",moovieListService.likeMoovieListStatusForUser(moovieListCard.getMoovieListId()));
        mav.addObject("likedCount",moovieListCard.getLikeCount());
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        final Map<String, String> queries = new HashMap<>();
        queries.put("list",list);
        queries.put("orderBy", orderBy);
        queries.put("order", order);
        String urlBase = UriComponentsBuilder.newInstance().path("/featuredList/{list}")
                .query("orderBy={orderBy}&order={order}")
                .buildAndExpand(queries).toUriString();
        mav.addObject("urlBase", urlBase);
        return mav;
    }


    @RequestMapping(value = "/createListAction", method = RequestMethod.POST)
    public ModelAndView createListAction(@Valid @ModelAttribute("ListForm") final CreateListForm form, final BindingResult errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return createList(null, null, null, null, null, 1, form);
        }
        MoovieList list = null;
        try {
            list = moovieListService.createMoovieListWithContent(form.getListName(), MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), form.getListDescription(), form.getMediaIdsList());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating list");
            return new ModelAndView("redirect:/createList");
        }
        int id = list.getMoovieListId();
        return new ModelAndView("redirect:/list/" + id);
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ModelAndView putLike(@RequestParam("listId") int listId) {
        moovieListService.likeMoovieList(listId);
        return new ModelAndView("redirect:/list/" + listId);
    }

    @RequestMapping(value = "/insertMediaToList", method = RequestMethod.POST)
    public ModelAndView insertMediaToList(@RequestParam("listId") int listId, @RequestParam("mediaId") int mediaId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            moovieListService.insertMediaIntoMoovieList(listId, Collections.singletonList(mediaId));
            redirectAttributes.addFlashAttribute("successMessage", "Media has been successfully added to ");
        } catch (UnableToInsertIntoDatabase exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to insert media into the list. Already in ");
        }
        redirectAttributes.addFlashAttribute("insertedMooovieList", moovieListService.getMoovieListCardById(listId));

        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("list")) {
            return new ModelAndView("redirect:" + referer);
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping(value = "/deleteMediaFromList", method = RequestMethod.POST)
    public ModelAndView deleteMediaFromList(@RequestParam("listId") int listId,@RequestParam("mediaId") int mediaId,HttpServletRequest request,RedirectAttributes redirectAttributes){
        try{
            moovieListService.deleteMediaFromMoovieList(listId,mediaId);
            redirectAttributes.addFlashAttribute("successMessage", "Media has been successfully deleted from ");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete media from the list ");
    }
        String referer = request.getHeader("Referer");
        redirectAttributes.addFlashAttribute("insertedMooovieList", moovieListService.getMoovieListCardById(listId));
        return new ModelAndView("redirect:" + referer);
    }
}
