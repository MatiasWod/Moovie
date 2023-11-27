package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CreateListForm;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

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
    private ProviderService providerService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

    @RequestMapping("/lists")
    public ModelAndView lists(  @RequestParam(value= "orderBy", defaultValue = "moovielistid") final String orderBy,
                                @RequestParam(value= "order", defaultValue = "desc") final String order,
                                @RequestParam(value = "search", required = false) final String search,
                                @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        LOGGER.info("Attempting to get lists for /lists.");

        final ModelAndView mav = new ModelAndView("moovieList/viewLists");

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
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }        LOGGER.info("Returned lists for /lists.");
        return mav;
    }

    @RequestMapping("/createList")
    public ModelAndView createList(@RequestParam(value = "g", required = false) List<String> genres,
                                   @RequestParam(value = "m", required = false,defaultValue = "All") String media,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam(value = "l", required = false) final List<String> lang,
                                   @RequestParam(value = "status", required = false) final List<String> status,
                                   @RequestParam(value = "providers", required = false) List<String> providers,
                                   @RequestParam(value="orderBy", defaultValue = "tmdbRating") final String orderBy,
                                   @RequestParam(value="order", defaultValue = "desc") final String order,
                                   @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                   @ModelAttribute("ListForm") final CreateListForm form) {
        LOGGER.info("Attempting to get lists for /createLists.");
        final ModelAndView mav = new ModelAndView("main/createList");
        int numberOfPages;
        int mediaCount;
        mav.addObject("searchMode",false);
        if (media.equals("All")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query, null, genres, providers,status, lang,orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query,null,genres,null, providers,status);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), query, null, genres, providers,providers,status,orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_MOVIE.getType(), query, null, genres, null, providers,status);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), query, null, genres, providers,providers,status,orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_TVSERIE.getType(), query, null, genres, null, providers,status);
        }
        numberOfPages = (int) Math.ceil(mediaCount * 1.0 / PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        // filter buttons
        mav.addObject("genresList", genreService.getAllGenres());
        mav.addObject("providersList", providerService.getAllProviders());
        mav.addObject("statusList",statusService.getAllStatus());
        mav.addObject("langList",languageService.getAllLanguages());

        LOGGER.info("Returned lists for /createLists.");
        return mav;
    }

    @RequestMapping("/profile/{username}/watchedList")
    public ModelAndView watchedlist(@PathVariable("username") final String username) {
        LOGGER.info("Attempting to get WatchedLists for /profile");
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watched",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        LOGGER.info("Returned WatchedLists for /profile");
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1,1,null);
    }

    @RequestMapping("/profile/{username}/watchList")
    public ModelAndView watchlist(@PathVariable("username") final String username) {
        LOGGER.info("Attempting to get WatchLists for /profile");
        final List<MoovieListCard> moovieListCards = moovieListService.getMoovieListCards("Watchlist",username,MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),0);
        LOGGER.info("Returned WatchList for /profile");
        return list(moovieListCards.get(0).getMoovieListId(),null,null,1,1,null);
    }

    @RequestMapping("/list/{id:\\d+}")
    public ModelAndView list(@PathVariable("id") final int moovieListId,
                             @RequestParam(value="orderBy", defaultValue = "customOrder") final String orderBy,
                             @RequestParam(value="order", defaultValue = "asc") final String order,
                             @RequestParam(value = "page", defaultValue = "1") final int pageNumber,
                             @RequestParam(value = "pageReview", defaultValue = "1") final int pageReviewNumber,
                             @ModelAttribute("createReviewForm") final CreateReviewForm createReviewForm
                             ) {
        LOGGER.info("Attempting to get list with id: {} for /list.", moovieListId);

        final ModelAndView mav = new ModelAndView("moovieList/moovieList");
        int pagesSize= PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        try{
            MoovieListDetails myList=moovieListService.getMoovieListDetails(moovieListId,null,null,orderBy,order,pagesSize,pageNumber - 1);
            List<MoovieListReview> listReview = reviewService.getMoovieListReviewsByMoovieListId(moovieListId,PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), 0);
            final MoovieListCard moovieListCard = myList.getCard();
            int mediaCountForMoovieList =moovieListCard.getSize();
            int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);

            mav.addObject("listReview", listReview);
            mav.addObject("moovieList",moovieListCard);
            mav.addObject("mediaList",myList.getContent());
            try {
                User currentUser=userService.getInfoOfMyUser();
                mav.addObject("currentUser", currentUser);
                mav.addObject("watchedCount",myList.getCard().getCurrentUserWatchAmount());
                mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
                mav.addObject("watchlistId",moovieListService.getMoovieListCards("Watchlist",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
                mav.addObject("isOwner", currentUser.getUsername().equals(moovieListCard.getUsername()));
            }catch (Exception e){
                mav.addObject("watchedCount",0);
            }
            moovieListCard(orderBy, pageNumber, mav, moovieListCard, mediaCountForMoovieList, numberOfPages);
            mav.addObject("RecomendedListsCards",moovieListService.getRecommendedMoovieListCards(moovieListId,4,0));

            try{
                mav.addObject("currentUsername", userService.getInfoOfMyUser().getUsername());
            }catch(Exception e){
                mav.addObject("currentUsername", "?????");
            }
            mav.addObject("pagingSize",pagesSize);
            mav.addObject("currentPage",pageNumber - 1);
            mav.addObject("numberOfPages",numberOfPages);

            mav.addObject("reviews", reviewService.getMoovieListReviewsByMoovieListId(moovieListId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
            int numberOfReviews = reviewService.getMoovieListReviewByMoovieListIdCount(moovieListId);
            mav.addObject("currentReviewPage", pageReviewNumber -1 );
            int numberOfReviewPages = (int) Math.ceil(numberOfReviews * 1.0 / PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
            mav.addObject("numberOfReviewPages",numberOfReviewPages);


            final Map<String, String> queries = new HashMap<>();
            queries.put("id", Integer.toString(moovieListId));
            queries.put("orderBy", orderBy);
            queries.put("order", order);
            String urlBase = UriComponentsBuilder.newInstance().path("/list/{id}").query("orderBy={orderBy}&order={order}").buildAndExpand(queries).toUriString();
            mav.addObject("urlBase", urlBase);

            LOGGER.info("Returned list with id: {} for /list.", moovieListId);
            return mav;
        } catch (MoovieListNotFoundException e){
            LOGGER.info("Failed to return list with id: {} for /list. {} ", moovieListId, e);
            return new ModelAndView("errors/404").addObject("extrainfo", "Error retrieving list, no list for id: "+ moovieListId);
        }
    }

    @RequestMapping(value = "/editList/{id:\\d+}")
    public ModelAndView editList(@PathVariable("id") final int moovieListId, @RequestParam(value = "page", defaultValue = "1") final int pageNumber) {
        LOGGER.info("Attempting to get list with id: {} , for /editList", moovieListId);
        final ModelAndView mav = new ModelAndView("moovieList/editList");
        try {
            User currentUser = userService.getInfoOfMyUser();
            mav.addObject("currentUser", currentUser);
            if (!currentUser.getUsername().equals(moovieListService.getMoovieListCardById(moovieListId).getUsername())) {
                LOGGER.info("Failed to get list with id: {} , for /editList", moovieListId);
                return new ModelAndView("errors/404");
            }
        } catch (Exception e) {
            LOGGER.info("Failed to get list with id: {} , for /editList", moovieListId);
            return new ModelAndView("errors/404");
        }
        int pagesSize = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        MoovieListDetails myList = moovieListService.getMoovieListDetails(moovieListId, null, null, MediaFilters.CUSTOM_ORDER.getFilter(), MediaFilters.ASC.getFilter(), pagesSize, pageNumber - 1);
        int mediaCountForMoovieList =myList.getCard().getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);
        mav.addObject("pagingSize",pagesSize);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("moovieList", myList.getCard());
        mav.addObject("mediaList", myList.getContent());

        mav.addObject("recommendedList", moovieListService.getRecommendedMediaToAdd(moovieListId, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize() ));

        LOGGER.info("Returned list with id: {} for /editList.", moovieListId);
        return mav;
    }

    @RequestMapping(value = "/updateMoovieListOrder/{listId:\\d+}", method = RequestMethod.POST)
    public ModelAndView updateMoovieListOrder(@PathVariable final int listId,
                                              @RequestParam(value = "toPrevArray") final int[] toPrevArray,
                                              @RequestParam(value = "currentArray") final int[] currentArray,
                                              @RequestParam(value = "toNextArray") final int[] toNextArray,
                                              @RequestParam(value="currentPageNumber") final int currentPageNumber){
        try {
            moovieListService.updateMoovieListOrder(listId,currentPageNumber,toPrevArray,currentArray,toNextArray);
            return new ModelAndView("redirect:/list/" + listId);
        }catch (InvalidAccessToResourceException e) {
            return new ModelAndView("errors/404").addObject("extrainfo", "Can't modify list that are not your own");
        }
//        } catch (Exception e) {
//            return new ModelAndView("helloworld/404").addObject("extrainfo", "Failed updating order");
//        }
    }


    @RequestMapping(value = "/featuredList/{list}")
    public ModelAndView featuredList(
                                     @PathVariable("list") final String list,
                                     @RequestParam(value="orderBy",required = false) final String orderBy,
                                     @RequestParam(value="order",required = false) final String order,
                                     @RequestParam(value = "page",defaultValue = "1") final int pageNumber){
        LOGGER.info("Attempting to get featured list : {} for /featuredlist.", list);

        final ModelAndView mav = new ModelAndView("moovieList/moovieList");
        int pagesSize= PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
        MoovieListCard moovieListCard;
        List<Media> moovieListContentList;
        Optional<FeaturedMoovieListEnum> topRatedMoovieLists = FeaturedMoovieListEnum.fromString(list);
        if(topRatedMoovieLists.isPresent()){
            moovieListCard = moovieListService.getMoovieListCards(topRatedMoovieLists.get().getName(),"Moovie",MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PUBLIC.getType(),null,null,
                    1,0).get(0); //Solo hay una lista de Moovie con ese nombre, entonces solo traigo esa lista
            moovieListContentList = moovieListService.getFeaturedMoovieListContent(topRatedMoovieLists.get().getType(), topRatedMoovieLists.get().getOrder() ,orderBy,
                    order,pagesSize,pageNumber - 1);
            mav.addObject("watchedCount",moovieListService.countWatchedFeaturedMoovieListContent(topRatedMoovieLists.get().getType(), topRatedMoovieLists.get().getOrder()));
        }
        else {
            LOGGER.info("Failed to return featured list : {} for /featuredlist.", list);
            return new ModelAndView("errors/404");
        }
        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("currentUser", currentUser);
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("watchlistId",moovieListService.getMoovieListCards("Watchlist",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("isOwner", currentUser.getUsername().equals(moovieListCard.getUsername()));
        }catch (Exception e){
            mav.addObject("watchedCount",0);
        }
        int mediaCountForMoovieList = PagingSizes.FEATURED_MOOVIE_LIST_DEFAULT_TOTAL_CONTENT.getSize();
        int numberOfPages = (int) Math.ceil(mediaCountForMoovieList * 1.0 / pagesSize);
        mav.addObject("moovieList",moovieListCard);
        mav.addObject("mediaList",moovieListContentList);
        moovieListCard(orderBy, pageNumber, mav, moovieListCard, mediaCountForMoovieList, numberOfPages);
        final Map<String, String> queries = new HashMap<>();
        queries.put("list",list);
        queries.put("orderBy", orderBy);
        queries.put("order", order);
        String urlBase = UriComponentsBuilder.newInstance().path("/featuredList/{list}")
                .query("orderBy={orderBy}&order={order}")
                .buildAndExpand(queries).toUriString();
        mav.addObject("urlBase", urlBase);

        LOGGER.info("Returned featured list: {} for /featuredlist.", list);
        return mav;
    }

    private void moovieListCard(@RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(value = "page", defaultValue = "1") int pageNumber, ModelAndView mav, MoovieListCard moovieListCard, int mediaCountForMoovieList, int numberOfPages) {
        mav.addObject("listCount",mediaCountForMoovieList);
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);
        mav.addObject("listOwner",moovieListCard.getUsername());
        mav.addObject("orderBy", orderBy);
        mav.addObject("publicType", MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
    }


    @RequestMapping(value = "/createListAction", method = RequestMethod.POST)
    public ModelAndView createListAction(@Valid @ModelAttribute("ListForm") final CreateListForm form, final BindingResult errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return createList(null, null, null, null, null, null, null, null, 1, form);
        }

        try {
            MoovieList ml = moovieListService.createMoovieList(form.getListName(), MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), form.getListDescription());
            try{
                int listId = moovieListService.insertMediaIntoMoovieList(ml.getMoovieListId(), form.getMediaIdsList()).getMoovieListId();
                return new ModelAndView("redirect:/list/" + listId);
            } catch (Exception e){
                redirectAttributes.addFlashAttribute("errorMessage", "createList.mediaInsertError");
                return new ModelAndView("redirect:/createList");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "createList.errorCreatingList");
            return new ModelAndView("redirect:/createList");
        }

    }

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ModelAndView putLike(@RequestParam("listId") int listId) {
        try{
            moovieListService.likeMoovieList(listId);
        } catch(Exception e){
            //Just reload the page
            return new ModelAndView("redirect:/list/" + listId);
        }
        return new ModelAndView("redirect:/list/" + listId);
    }

    @RequestMapping(value = "/followList", method = RequestMethod.POST)
    public ModelAndView followList(@RequestParam("listId") int listId) {
        try{
            moovieListService.followMoovieList(listId);
        } catch(Exception e){
            //Just reload the page
            return new ModelAndView("redirect:/list/" + listId);
        }
        return new ModelAndView("redirect:/list/" + listId);
    }

    @RequestMapping(value = "/insertMediaToList", method = RequestMethod.POST)
    public ModelAndView insertMediaToList(@RequestParam("listId") int listId, @RequestParam("mediaId") int mediaId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        LOGGER.info("Attempting to insert media: {} to list with id: {}.",mediaId,listId);
        try {
            moovieListService.insertMediaIntoMoovieList(listId, Collections.singletonList(mediaId));
            redirectAttributes.addFlashAttribute("successMessage",  messageSource.getMessage("list.mediaAddedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToInsertIntoDatabase exception) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.mediaAddedFailure",null, LocaleContextHolder.getLocale()));
        }
        redirectAttributes.addFlashAttribute("insertedMooovieList", moovieListService.getMoovieListCardById(listId));

        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("editList")||referer.contains("list") || referer.contains("featuredList") || referer.contains("discover") || referer.contains("cast")) {
            return new ModelAndView("redirect:" + referer);
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping(value = "/deleteMediaFromList", method = RequestMethod.POST)
    public ModelAndView deleteMediaFromList(@RequestParam("listId") int listId,@RequestParam("mediaId") int mediaId,HttpServletRequest request,RedirectAttributes redirectAttributes){
        try{
            moovieListService.deleteMediaFromMoovieList(listId,mediaId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.mediaDeletedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.mediaDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        redirectAttributes.addFlashAttribute("insertedMooovieList", moovieListService.getMoovieListCardById(listId));

        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("editList")||referer.contains("list") || referer.contains("featuredList") || referer.contains("discover") || referer.contains("cast")) {
            return new ModelAndView("redirect:" + referer);
        } else {
            return new ModelAndView("redirect:/");
        }
    }


    @RequestMapping(value = "/deleteMoovieList/{moovieListId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieList(@PathVariable int moovieListId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            moovieListService.deleteMoovieList(moovieListId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.moovieListDeletedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.moovieListDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("list")) {
            return new ModelAndView("redirect:/lists");
        }  else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=ml");
        }
        return new ModelAndView("redirect:/lists");
    }

    @RequestMapping(value = "/MoovieListReview", method = RequestMethod.POST)
    public ModelAndView createMoovieListReview(@Valid @ModelAttribute("createReviewForm") final CreateReviewForm createReviewForm, final BindingResult errors, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return list(createReviewForm.getMediaId(),"tmdbRating", "asc", 1, 1,createReviewForm);
        }
        try{
            reviewService.createReview(createReviewForm.getMediaId(), createReviewForm.getRating(), createReviewForm.getReviewContent(), ReviewTypes.REVIEW_MOOVIE_LIST);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.commentCreatedSuccess",null, LocaleContextHolder.getLocale()));
        } catch(Exception e1) {
            try {
                reviewService.editReview(createReviewForm.getMediaId(), createReviewForm.getRating(), createReviewForm.getReviewContent(), ReviewTypes.REVIEW_MOOVIE_LIST);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.commentEditedSuccess",null, LocaleContextHolder.getLocale()));
            } catch (Exception e2) {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.commentCreatedFailure",null, LocaleContextHolder.getLocale()));
            }
        }
        return new ModelAndView("redirect:/list/" + createReviewForm.getMediaId());
    }

    @RequestMapping(value= "/likeMoovieListReview", method = RequestMethod.POST)
    public ModelAndView likeMoovieListReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.likeReview(reviewId, ReviewTypes.REVIEW_MOOVIE_LIST);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.reviewLikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.reviewLikedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/list/" + mediaId);
    }

    @RequestMapping(value= "/unlikeMoovieListReview", method = RequestMethod.POST)
    public ModelAndView unlikeMoovieListReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.removeLikeReview(reviewId, ReviewTypes.REVIEW_MOOVIE_LIST);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.reviewUnlikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.reviewUnlikedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/list/" + mediaId);
    }

    @RequestMapping(value = "/deleteUserMoovieListReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieListReview(@RequestParam("reviewId") int reviewId,
                                               RedirectAttributes redirectAttributes,
                                               @PathVariable int mediaId,
                                               HttpServletRequest request) {
        try {
            reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MOOVIE_LIST);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("list")) {
            return new ModelAndView("redirect:/list/" + mediaId);
        } else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=mlReviews");
        }
        return new ModelAndView("redirect:/list/" + mediaId);
    }

}
