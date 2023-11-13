package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.MediaNotFoundException;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.CreateReviewForm;
import jdk.net.SocketFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private DatabaseModifierService dmsService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private CommentService commentService;


    private static final Logger LOGGER = LoggerFactory.getLogger(MediaController.class);



    @RequestMapping("/testing")
    public ModelAndView test(){
        final ModelAndView mav = new ModelAndView("helloworld/test");
        mav.addObject("ml", moovieListService.getRecommendedMediaToAdd(37,4));
        return mav;
    }

    @RequestMapping("/adolfoTest")
    public ModelAndView adolfoTest(){
        final ModelAndView mav = new ModelAndView("helloworld/adolfoTesting");
        mav.addObject("providerList", providerService.getAllProviders());
        mav.addObject("user", userService.getInfoOfMyUser());
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView home() {

        LOGGER.info("Attempting to get media for /.");
        final ModelAndView mav = new ModelAndView("helloworld/index");
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
        Media ml = mediaService.getMediaById(10);
        mav.addObject("ml", ml);
        return mav;




        
        /*
        final ModelAndView mav = new ModelAndView("helloworld/test");

        dmsService.updateGenres();
        dmsService.updateProviders();
        return mav;*/
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "query") String query){
        LOGGER.info("Attempting to get media for /search.");

        final ModelAndView mav = new ModelAndView("helloworld/search");
        // Aca se realizan 3 queries. Para poder notificar correctamente al JSP de las listas que va a recibir, primero se corre el getMediaCount
        int nameMediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), query, null, null, null, null, null);
        int creditMediaCount = mediaService.getMediaCount(MediaTypes.TYPE_ALL.getType(), null, query, null, null, null, null);
        int usersCount = userService.getSearchCount(query);
        int moovieListCount = moovieListService.getMoovieListCardsCount(query,null,MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),0);

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
        // Credited media query
        if (creditMediaCount > 0){
            mav.addObject("creditMediaFlag", true);
            mav.addObject("creditMedia", mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), null, query, null, null,null,null, "tmdbRating", "desc",resultSizeLimit,0 ));
        }else{
            mav.addObject("creditMediaFlag",false);
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
        final ModelAndView mav = new ModelAndView("helloworld/discover");

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
            final ModelAndView mav = new ModelAndView("helloworld/404");
            mav.addObject("extraInfo", e.getMessage());
            return mav;
        }
        
        final ModelAndView mav = new ModelAndView("helloworld/details");
        try{
            String username =  userService.getInfoOfMyUser().getUsername();
            mav.addObject("currentUsername", username);
            mav.addObject("publicLists", moovieListService.getMoovieListCards(null, username, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),null,null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0));
            mav.addObject("privateLists", moovieListService.getMoovieListCards(null, username, MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), 0));
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
                                     @ModelAttribute("commentForm") final CommentForm commentForm,
                                     final BindingResult errors,
                                     RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return details(form.getMediaId(),1, form, commentForm);
        }
        try{
            reviewService.createReview(form.getMediaId(), form.getRating(), form.getReviewContent(), ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", "Review has been successfully created.");
        } catch(Exception e1) {
            try {
                reviewService.editReview(form.getMediaId(), form.getRating(), form.getReviewContent(), ReviewTypes.REVIEW_MEDIA);
                redirectAttributes.addFlashAttribute("successMessage", "Review has been successfully edited.");
            } catch (Exception e2) {
                redirectAttributes.addFlashAttribute("errorMessage", "Couldn't create review, you already reviewed this.");
            }
        }
        return new ModelAndView("redirect:/details/" + form.getMediaId());
    }

    @RequestMapping(value= "/likeReview", method = RequestMethod.POST)
    public ModelAndView likeReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.likeReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", "Review has been successfully liked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't like review.");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value= "/unlikeReview", method = RequestMethod.POST)
    public ModelAndView unlikeReview(@RequestParam int reviewId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            reviewService.removeLikeReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", "Review has been successfully unliked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't unlike review.");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/deleteUserReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId,RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", "Review successfully deleted");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting review");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping("/review/{id:\\d+}")
    public ModelAndView review(@PathVariable int id, @ModelAttribute("commentForm") CommentForm commentForm) {
        final ModelAndView mav = new ModelAndView("helloworld/review");
        List<Media> movieList = mediaService.getMedia(MediaTypes.TYPE_MOVIE.getType(), null, null,
                null, null, null, null,MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0);
        mav.addObject("movieList", movieList);
        mav.addObject("review", reviewService.getReviewById(id));
        mav.addObject("currentUsername", userService.getInfoOfMyUser());
        return mav;
    }

    @RequestMapping(value= "/likeComment", method = RequestMethod.POST)
    public ModelAndView likeComment(@RequestParam int commentId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            commentService.likeComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Comment has been successfully liked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't like comment.");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value= "/dislikeComment", method = RequestMethod.POST)
    public ModelAndView dislikeComment(@RequestParam int commentId,@RequestParam int mediaId, RedirectAttributes redirectAttributes){
        try {
            commentService.dislikeComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", "Comment has been successfully disliked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't dislike comment.");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/createcomment", method = RequestMethod.POST)
    public ModelAndView createComment(@ModelAttribute("detailsForm") final CreateReviewForm form,
                                      @Valid @ModelAttribute("commentForm") final CommentForm commentForm,
                                      final BindingResult errors, HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return details(form.getMediaId(),1, form, commentForm);
        }
        try{
            commentService.createComment(commentForm.getReviewId(),commentForm.getContent());
            redirectAttributes.addFlashAttribute("successMessage", "Comment has been successfully created.");
        } catch(Exception e1) {
            redirectAttributes.addFlashAttribute("errorMessage", "Couldn't create comment.");
        }

        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + commentForm.getListMediaId());
        } else if (referer.contains("review")) {
            return new ModelAndView("redirect:/review/" + commentForm.getReviewId());
        }
        return new ModelAndView("helloworld/index");
    }
}

