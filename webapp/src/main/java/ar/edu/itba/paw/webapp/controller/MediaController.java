package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.ActorNotFoundException;
import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.TV.TVCreators;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CommentForm;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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


    @Autowired
    private StatusService statusService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModeratorService moderatorService;


    private static final Logger LOGGER = LoggerFactory.getLogger(MediaController.class);


    @RequestMapping("/")
    public ModelAndView home() {

        LOGGER.info("Attempting to get media for /.");
        final ModelAndView mav = new ModelAndView("main/index");
        List<Media> movieList = mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), null, null,
                null, null, null, null, MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("movieList", movieList);

        List<Media> tvSerieList = mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), null, null,
                null, null, null,null, MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("tvList", tvSerieList);

        List<Media> popularTV = mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), null, null,
                null, null, null,null, MediaFilters.VOTECOUNT.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("tvListPopular", popularTV);

        List<Media> popularMovies = mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), null, null,
                null, null, null,null, MediaFilters.VOTECOUNT.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("movieListPopular", popularMovies);

        LOGGER.info("Returned media for /.");
        //Media ml = mediaService.getMediaById(10);
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        //mav.addObject("ml", ml);
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query") String query){
        LOGGER.info("Attempting to get media for /search.");

        int searchSizes = 3;

        final ModelAndView mav = new ModelAndView("main/search");
        // Aca se realizan 3 queries. Para poder notificar correctamente al JSP de las listas que va a recibir, primero se corre el getMediaCount
        int nameMediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query, null, null, null, null, null);
        int actorsCount = actorService.getActorsForQueryCount(query);
        int creatorsCount = mediaService.getDirectorsForQueryCount(query, searchSizes);
        int usersCount = userService.getSearchCount(query);
        int moovieListCount = moovieListService.getMoovieListCardsCount(query,null,MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),0);

        List<TVCreators> tvCreators = tvCreatorsService.getTVCreatorsForQuery(query,searchSizes);
        creatorsCount += tvCreators.size();

        if (query.isEmpty()){
            return mav;
        }

        int resultSizeLimit = 6;

        // Name media query
        if (nameMediaCount > 0){
            mav.addObject("nameMediaFlag", true);
            mav.addObject("nameMedia", mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query, null, null, null, null, null,"tmdbRating", "desc",resultSizeLimit,0 ));
        }else{
            mav.addObject("nameMediaFlag",false);
        }
        // Actors query
        if (actorsCount > 0){
            mav.addObject("actorsFlag", true);
            mav.addObject("actors", actorService.getActorsForQuery(query));
        }else{
            mav.addObject("actorsFlag",false);
        }
        // Creators/Directors query
        if (creatorsCount > 0){
            mav.addObject("creatorsFlag", true);
            mav.addObject("directors", mediaService.getDirectorsForQuery( query,searchSizes ));
            mav.addObject("creators", tvCreators);
        }else{
            mav.addObject("creatorsFlag",false);
        }
        // Users query
        if (usersCount > 0){
            mav.addObject("usersFlag", true);
            mav.addObject("usersList", userService.searchUsers(query, "username", "ASC" ,PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(),0));
        }else{
            mav.addObject("usersFlag",false);
        }
        // if (usersCount > 0) --> mav.addObject(userList, userService.searchUsers(query,...))

        // Moovielist query
        if(moovieListCount > 0){
            mav.addObject("moovieListFlag",true);
            mav.addObject("moovieListsList",moovieListService.getMoovieListCards(query,null,MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),null,null, resultSizeLimit, 0));
        }
        else {
            mav.addObject("moovieListFlag",false);
        }

        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }

        LOGGER.info("Returned media for /search.");
        return mav;
    }

    @RequestMapping("/discover")
    public ModelAndView discover(@RequestParam(value = "query", required = false) String query,
                               @RequestParam(value = "credit", required = false) String credit,
                               @RequestParam(value = "m", required = false, defaultValue = "All") String media,
                               @RequestParam(value = "g", required = false) List<String> genres,
                                 @RequestParam(value = "providers", required = false) List<String> providers,
                                 @RequestParam(value = "l", required = false) final List<String> lang,
                                 @RequestParam(value = "status", required = false) final List<String> status,
                                 @RequestParam(value="orderBy", defaultValue = "tmdbRating") final String orderBy,
                                 @RequestParam(value="order", defaultValue = "desc") final String order,
                               @RequestParam(value = "page", defaultValue = "1") final int pageNumber){
        LOGGER.info("Attempting to get media for /discover.");
        final ModelAndView mav = new ModelAndView("main/discover");

        mav.addObject("searchMode", (query != null && !query.isEmpty()));
        int mediaCount;



        if (media.equals("All")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), query, credit,  genres, providers, status, lang, orderBy, order, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query, credit, genres, providers, status, lang);
        }
        else if (media.equals("Movies")){
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), query, credit, genres, providers, status, lang, orderBy, order,   PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_MOVIE.getType(), query, credit, genres, providers, status, lang);
        }
        else{
            mav.addObject("mediaList",mediaService.getMedia(MediaTypes.TYPE_TVSERIE.getType(), query, credit, genres, providers, status, lang, orderBy, order,  PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
            mediaCount = mediaService.getMediaCount(MediaTypes.TYPE_TVSERIE.getType(), query, credit, genres, providers, status, lang);
        }

        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("currentUser", currentUser);
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("watchlistId",moovieListService.getMoovieListCards("Watchlist",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("showWatched",true);
        }catch (Exception e){
            mav.addObject("showWatched",false);
        }

        int numberOfPages = (int) Math.ceil(mediaCount * 1.0 / PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize());
        mav.addObject("numberOfPages",numberOfPages);
        mav.addObject("currentPage",pageNumber - 1);

        // filter buttons
        mav.addObject("genresList", genreService.getAllGenres());
        mav.addObject("providersList", providerService.getAllProviders());
        mav.addObject("statusList",statusService.getAllStatus());
        mav.addObject("langList",languageService.getAllLanguages());

        LOGGER.info("Returned media for /discover.");
        return mav;

    }

    @RequestMapping(value = "/details/{id:\\d+}")
    public ModelAndView details(@PathVariable("id") final int mediaId,
                                @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                @ModelAttribute("detailsForm") final CreateReviewForm form,
                                @ModelAttribute("commentForm") final CommentForm commentForm) {
        LOGGER.info("Attempting to get media with id: {} for /details.", mediaId);

        boolean type;
        try{
            type = mediaService.getMediaById(mediaId).isType();
        } catch (MediaNotFoundException e){
            LOGGER.info("Failed to get media with id: {} for /details.", mediaId);
            final ModelAndView mav = new ModelAndView("errors/404");
            mav.addObject("extraInfo", e.getMessage());
            return mav;
        }
        
        final ModelAndView mav = new ModelAndView("main/details");
        try{
            User user =  userService.getInfoOfMyUser();
            String username=user.getUsername();
            int userId=user.getUserId();
            mav.addObject("currentUser", user);
            try {
                mav.addObject("userReview", reviewService.getReviewByMediaIdAndUsername(mediaId, userId, ReviewTypes.REVIEW_MEDIA));
            }catch (Exception e){
                mav.addObject("userReview", null);
            }
            try {
                mav.addObject("publicLists", moovieListService.getMoovieListCards(null, username, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),null,null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0));
            }catch (Exception e) {
                mav.addObject("publicLists", null);
            }
            try {
                mav.addObject("privateLists", moovieListService.getMoovieListCards(null, username, MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0));
            }catch (Exception e) {
                mav.addObject("privateLists", null);
            }
        } catch (Exception ignored) {
        }

        Media media;

        if(!type){
            media = mediaService.getMovieById(mediaId);
        } else{
            media =  mediaService.getTvById(mediaId);
            mav.addObject("creators", tvCreatorsService.getTvCreatorsByMediaId(mediaId));
        }

        mav.addObject("media", media);

        mav.addObject("genresList", media.getGenres());
        mav.addObject("actorsList", actorService.getAllActorsForMedia(mediaId));
        mav.addObject("providersList", media.getProviders());


        //Pagination of reviews
        mav.addObject("reviewsList", reviewService.getReviewsByMediaId(mediaId,PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
        mav.addObject("currentPage",pageNumber - 1);
        int totalReviewsForMedia = reviewService.getReviewsByMediaIdCount(mediaId);
        int numberOfPages = (int) Math.ceil(totalReviewsForMedia * 1.0 / PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
        mav.addObject("numberOfPages",numberOfPages);

        LOGGER.info("Returned media with id: {} for /details.", mediaId);
        return mav;
    }

    @RequestMapping(value = "/createrating", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("detailsForm") final CreateReviewForm form,
                                     final BindingResult errors,
                                     RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return details(form.getMediaId(),1, form, null);
        }
        try{
            reviewService.createReview(form.getMediaId(), form.getRating(), form.getReviewContent(), ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.reviewCreated",null, LocaleContextHolder.getLocale()));
        } catch(Exception e1) {
            try {
                reviewService.editReview(form.getMediaId(), form.getRating(), form.getReviewContent(), ReviewTypes.REVIEW_MEDIA);
                redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.editReviewSuccess",null, LocaleContextHolder.getLocale()));
            } catch (Exception e2) {
                redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("details.editReviewFailure",null, LocaleContextHolder.getLocale()));
            }
        }
        return new ModelAndView("redirect:/details/" + form.getMediaId());
    }

    @RequestMapping(value= "/likeReview", method = RequestMethod.POST)
    public ModelAndView likeReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.likeReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.reviewLikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("details.reviewLikedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value= "/unlikeReview", method = RequestMethod.POST)
    public ModelAndView unlikeReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.removeLikeReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.reviewUnlikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",  messageSource.getMessage("details.reviewUnlikedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/deleteUserReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable int mediaId,
                                     HttpServletRequest request) {
        try {
            reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage",  messageSource.getMessage("details.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=reviews");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/cast/{type}/{id:\\d+}")
    public ModelAndView actor(@PathVariable String type, @PathVariable int id){
        final ModelAndView mav = new ModelAndView("main/cast");
        try {
            User currentUser=userService.getInfoOfMyUser();
            mav.addObject("currentUser", currentUser);
            mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("watchlistId",moovieListService.getMoovieListCards("Watchlist",currentUser.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
            mav.addObject("showWatched",true);
        }catch (Exception e){
            mav.addObject("showWatched",false);
        }
        switch(type){
            case "actor" :
                try{
                    mav.addObject("type", type);
                    mav.addObject("actor", actorService.getActorById(id));
                    return mav;
                } catch(ActorNotFoundException e){
                    return new ModelAndView("helloword/404");
                }
            case "creator" :
                try{
                    mav.addObject("type", type);
                    mav.addObject("tvCreator", tvCreatorsService.getTvCreatorById(id));
//                    mav.addObject("media", mediaService.getMediaForCreatorId(id));
                    return mav;
                } catch(ActorNotFoundException e){
                    return new ModelAndView("helloword/404");
                }
            case "director" :
                try{
                    mav.addObject("type", type);
                    mav.addObject("directorMedia", mediaService.getMediaForDirectorId(id));
                    return mav;
                } catch(ActorNotFoundException e){
                    return new ModelAndView("helloword/404");
                }
        }
        return new ModelAndView("helloword/404");
    }

    @RequestMapping("/review/{mediaId:\\d+}/{id:\\d+}")
    public ModelAndView review(@PathVariable int id,@PathVariable int mediaId ,@ModelAttribute("commentForm") CommentForm commentForm) {
        final ModelAndView mav = new ModelAndView("main/review");
        List<Media> movieList = mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), null, null,
                null, null, null, null,MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("movieList", movieList);
        mav.addObject("mediaId", mediaId);
        mav.addObject("review", reviewService.getReviewById(id));
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        return mav;
    }

    @RequestMapping(value= "/likeComment", method = RequestMethod.POST)
    public ModelAndView likeComment(@RequestParam int commentId,@RequestParam int mediaId,
                                    RedirectAttributes redirectAttributes,HttpServletRequest request) {
        try {
            commentService.likeComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.commentLikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("details.commentLikedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("reivew")) {
            return new ModelAndView("redirect:/review/" + mediaId);
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value= "/dislikeComment", method = RequestMethod.POST)
    public ModelAndView dislikeComment(@RequestParam int commentId,@RequestParam int mediaId,
                                       RedirectAttributes redirectAttributes, HttpServletRequest request){
        try {
            commentService.dislikeComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.commentUnlikedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("details.commentUnlikedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        } else if (referer.contains("reivew")) {
            return new ModelAndView("redirect:/review/" + mediaId);
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/createcomment", method = RequestMethod.POST)
    public ModelAndView createComment(@Valid @ModelAttribute("commentForm") final CommentForm commentForm,
                                      final BindingResult errors, HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return details(commentForm.getListMediaId(),1, null, commentForm);
        }
        try{
            commentService.createComment(commentForm.getReviewId(),commentForm.getContent());
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("details.commentCreatedSuccess",null, LocaleContextHolder.getLocale()));
        } catch(Exception e1) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("details.commentCreatedFailure",null, LocaleContextHolder.getLocale()));
        }

        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + commentForm.getListMediaId());
        } else if (referer.contains("review")) {
            return new ModelAndView("redirect:/review/" + commentForm.getListMediaId() + "/" + commentForm.getReviewId());
        }
        return new ModelAndView("main/index");
    }
}

