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
import java.util.*;
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

    @Autowired
    private ProviderService providerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(  @RequestParam(value= "orderBy", defaultValue = "moovielistid") final String orderBy,
                                @RequestParam(value= "order", defaultValue = "desc") final String order,
                                @RequestParam(value = "search", required = false) final String search,
                                @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        final ModelAndView mav = new ModelAndView("helloworld/viewLists");

        mav.addObject("showLists", moovieListService.getMoovieListCards(search, null, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                orderBy, order,
                PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber - 1));

        int listCount = moovieListService.getMoovieListCardsCount(search,null,MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber - 1);
        int numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        final Map<String, String> queries = new HashMap<>();
        queries.put("orderBy",orderBy);
        queries.put("order",order);
        queries.put("search", search);
        String urlBase = UriComponentsBuilder.newInstance().path("/lists").query("orderBy={orderBy}&order={order}&search={search}").buildAndExpand(queries).toUriString();
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
                                   @RequestParam(value = "m", required = false,defaultValue = "All") String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "providers", required = false) List<String> providers,
                                   @RequestParam(value="orderBy", defaultValue = "tmdbRating") final String orderBy,
                                   @RequestParam(value="order", defaultValue = "desc") final String order,
                                   @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                   @ModelAttribute("ListForm") final CreateListForm form) {

        final ModelAndView mav = new ModelAndView("helloworld/createList");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("All")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query, null, genres, providers, orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query,null,genres,null);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), query, null,  genres, providers, orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_MOVIE.getType(), query, null, genres, null);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), query, null,  genres,  providers,  orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_TVSERIE.getType(), query, null, genres, null);
        }
        numberOfPages = (int) Math.ceil(mediaCount * 1.0 / PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("genresList", genreService.getAllGenres());
        mav.addObject("providersList", providerService.getAllProviders());
        return mav;
    }

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watched",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1);
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watchlist",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1);
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId,
                             @RequestParam(value="orderBy", defaultValue = "customorder") final String orderBy,
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
            mav.addObject("watchedCount",myList.getCard().getCurrentUserWatchAmount());
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("isOwner", currentUser.getUsername().equals(moovieListCard.getUsername()));
        }catch (Exception e){
            mav.addObject("watchedCount",0);
        }
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("isFollowed",moovieListCard.isCurrentUserHasFollowed());
        mav.addObject("isLiked",myList.getCard().isCurrentUserHasLiked());
        mav.addObject("likedCount",moovieListCard.getLikeCount());
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        mav.addObject("publicType",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
        mav.addObject("RecomendedListsCards",moovieListService.getRecommendedMoovieListCards(moovieListId,4,0));

        Integer id = moovieListId;
        final Map<String, String> queries = new HashMap<>();
        queries.put("id",id.toString());
        queries.put("orderBy", orderBy);
        queries.put("order", order);
        String urlBase = UriComponentsBuilder.newInstance().path("/list/{id}").query("orderBy={orderBy}&order={order}").buildAndExpand(queries).toUriString();
        mav.addObject("urlBase", urlBase);
        return mav;
    }

    @RequestMapping(value = "/editList/{id:\\d+}")
    public ModelAndView editList(@PathVariable("id") final int moovieListId, @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            if (!currentUser.getUsername().equals(moovieListService.getMoovieListCardById(moovieListId).getUsername())) {
                return new ModelAndView("helloworld/404");
            }
        } catch (Exception e) {
            return new ModelAndView("helloworld/404");
        }
        final ModelAndView mav = new ModelAndView("helloworld/editList");
        int pagesSize = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        MoovieListDetails myList = moovieListService.getMoovieListDetails(moovieListId, null, null, "name", "asc", pagesSize, pageNumber - 1);
        int mediaCountForMoovieList =myList.getCard().getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);
        mav.addObject("pagingSize",pagesSize);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("moovieList", myList.getCard());
        mav.addObject("mediaList", myList.getContent());
        return mav;
    }

    @RequestMapping(value = "/updateMoovieListOrder/{listId:\\d+}", method = RequestMethod.POST)
    public ModelAndView updateMoovieListOrder(@PathVariable final int listId,
                                     @RequestParam(value = "toPrevArray") final int[] toPrevArray,
                                     @RequestParam(value = "currentArray") final int[] currentArray,
                                     @RequestParam(value = "toNextArray") final int[] toNextArray){
        try {
            User currentUser = userService.getInfoOfMyUser();
            if (!currentUser.getUsername().equals(moovieListService.getMoovieListCardById(listId).getUsername())) {
                return new ModelAndView("helloworld/404");
            }
        } catch (Exception e) {
            return new ModelAndView("helloworld/404");
        }
        moovieListService.updateMoovieListOrder(listId,toPrevArray,currentArray,toNextArray);
        return new ModelAndView("redirect:/list/" + listId);
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
        Optional<FeaturedMoovieListEnum> topRatedMoovieLists = FeaturedMoovieListEnum.fromString(list);
        if(topRatedMoovieLists.isPresent()){
            moovieListCard = moovieListService.getMoovieListCards(topRatedMoovieLists.get().getName(),"Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PUBLIC.getType(),null,null,
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(moovieListCard.getMoovieListId(),topRatedMoovieLists.get().getType(), topRatedMoovieLists.get().getOrder() ,orderBy,
                    order,pagesSize,pageNumber - 1);
            mav.addObject("watchedCount",moovieListService.countWatchedFeaturedMoovieListContent(moovieListCard.getMoovieListId(),MediaTypes.TYPE_MOVIE.getType(), topRatedMoovieLists.get().getOrder() ,orderBy,
                    order,100,0));
        }
        else {
            return new ModelAndView("helloworld/404");
        }
        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
        }catch (Exception e){

        }
        int mediaCountForMoovieList = PagingSizes.FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT.getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);
        mav.addObject("moovieList",moovieListCard);
        mav.addObject("mediaList",moovieListContentList);
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("isLiked",moovieListCard.isCurrentUserHasLiked());
        mav.addObject("isFollowed",moovieListCard.isCurrentUserHasFollowed());
        mav.addObject("likedCount",moovieListCard.getLikeCount());
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        mav.addObject("publicType",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
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
            return createList(null, null, null, null, null, null, 1, form);
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

    @RequestMapping(value = "/followList", method = RequestMethod.POST)
    public ModelAndView followList(@RequestParam("listId") int listId) {
        moovieListService.followMoovieList(listId);
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
        } else if (referer.contains("list") || referer.contains("featuredList")) {
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
