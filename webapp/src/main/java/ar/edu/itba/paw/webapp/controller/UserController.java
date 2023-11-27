package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.MediaFilters;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.MoovieUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    MoovieListService moovieListService;

    @Autowired
    MoovieUserDetailsService moovieUserDetailsService;

    @Autowired
    EmailService emailService;

    @Autowired
    MediaService mediaService;


    @Autowired
    private MessageSource messageSource;


    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        LOGGER.info("Attempting to get /register");
        LOGGER.info("Returned /register");
        return new ModelAndView("user/register").
                addObject("mediaList",
                mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), null,null,null,null,null,null,MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0));
    }

    @RequestMapping(value = "/register/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") final String token, RedirectAttributes redirectAttributes) {
        try {
            Token verificationToken = null;
            if (verificationTokenService.getToken(token).isPresent()){
                verificationToken = verificationTokenService.getToken(token).get();
            }
            if (userService.confirmRegister(verificationToken)) {
                UserDetails userDetails = moovieUserDetailsService.loadUserByUsername(userService.findUserById(verificationToken.getUserId()).getUsername());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                LOGGER.info("Auto login " + userDetails.getUsername() + " successfully!");
                LOGGER.info("Authentication data: " + usernamePasswordAuthenticationToken.getPrincipal() + " " + usernamePasswordAuthenticationToken.getCredentials() + " " + usernamePasswordAuthenticationToken.getAuthorities() + " " + usernamePasswordAuthenticationToken.getDetails() + " " + usernamePasswordAuthenticationToken.isAuthenticated());
                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
                LOGGER.info("Auto-login succesful for {} {}.",userDetails.getUsername(),userDetails.getAuthorities());
                return new ModelAndView("redirect:/");
            } else {
                redirectAttributes.addAttribute("token", token);
                redirectAttributes.addAttribute("message", messageSource.getMessage("user.tokenExpired",null, LocaleContextHolder.getLocale()));
                LOGGER.info("Auto-login failed.");
                return new ModelAndView("redirect:/register/resendEmail");
            }
        } catch (VerificationTokenNotFoundException e) {
            LOGGER.info("Auto-login failed.");
            return new ModelAndView("errors/404");
        }
    }




    @RequestMapping(value = "/register/tokentimedout")
    public ModelAndView tokenTimedOut(@RequestParam("token") final String token) {
        ModelAndView mav = new ModelAndView("user/tokenTimedOut");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/register/resendEmail", method = RequestMethod.POST)
    public ModelAndView resendEmail(@RequestParam("token") final String token,
                                    @RequestParam(value = "message", required = false) final String message,
                                    RedirectAttributes redirectAttributes) {
        LOGGER.info("Attempting to resend confirmation email.");
        redirectAttributes.addAttribute("token", token);
        if (message == null || message.isEmpty()) {
            redirectAttributes.addAttribute("message", messageSource.getMessage("user.resendVerification",null, LocaleContextHolder.getLocale()));
        } else {
            redirectAttributes.addAttribute("message", message);
        }
        ModelAndView mav = new ModelAndView("redirect:/register/sentEmail");
        Token toResendToken = verificationTokenService.getToken(token).orElseThrow(VerificationTokenNotFoundException::new);
        userService.resendVerificationEmail(toResendToken);
        LOGGER.info("Confirmation email was resent.");
        return mav;
    }



    @RequestMapping("/login")
    public ModelAndView login() {
        LOGGER.info("Attempting to get /register");
        LOGGER.info("Returned /register");
        return new ModelAndView("user/login")
                .addObject("mediaList",
                        mediaService.getMedia(MediaTypes.TYPE_ALL.getType(), null,null,null,null,null,null, MediaFilters.TMDBRATING.getFilter(), MediaFilters.DESC.getFilter(), PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 0));
    }

    @RequestMapping("/profile/{username:.+}")
    public ModelAndView profilePage(@PathVariable String username,
                                    @RequestParam( value = "list", required = false) String list,
                                    @RequestParam(value = "page",defaultValue = "1") final int pageNumber,
                                    @RequestParam(value="orderBy", defaultValue = "customOrder") final String orderBy,
                                    @RequestParam(value="order", defaultValue = "asc") final String order
                                    ){
        LOGGER.info("Attempting to get user with username: {} for /profile.", username);
        try{
            Profile requestedProfile = userService.getProfileByUsername(username);

            ModelAndView mav = new ModelAndView("user/profile");

            int listCount;
            int numberOfPages;
            final Map<String, String> queries = new HashMap<>();
            queries.put("username",username);

            mav.addObject("profile",requestedProfile);
            mav.addObject("isMe",userService.isUsernameMe(username));
            try {
                mav.addObject("currentUser", userService.getInfoOfMyUser()); //hace falta para el navBar
            } catch (Exception e) {
                // do nothing
            }
            if (list != null){
                switch (list) {
                    case "watched-list":
                        MoovieListDetails watchedDetails = moovieListService.getMoovieListDetails( -1 , "Watched" , username,orderBy, order,PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),pageNumber-1);
                        listCount = watchedDetails.getCard().getSize();
                        queries.put("list","watched-list");
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
                        mav.addObject("listDetails",watchedDetails);
                        mav.addObject("moovieList",watchedDetails.getCard());
                        mav.addObject("mediaList",watchedDetails.getContent());
                        mav.addObject("watchedCount",watchedDetails.getCard().getCurrentUserWatchAmount());
                        mav.addObject("watchedListId",watchedDetails.getCard().getMoovieListId());
                        mav.addObject("listCount",watchedDetails.getCard().getSize());
                        mav.addObject("numberOfPages",numberOfPages);
                        mav.addObject("currentPage",pageNumber - 1);
                        mav.addObject("listOwner",watchedDetails.getCard().getUsername());
                        mav.addObject("orderBy", orderBy);
                        mav.addObject("order", order);
                        mav.addObject("publicType",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
                        break;
                    case "watchlist":
                            MoovieListDetails watchlistDetails = moovieListService.getMoovieListDetails(-1, "Watchlist" , username, orderBy, order,PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),pageNumber-1);
                        listCount = watchlistDetails.getCard().getSize();
                        queries.put("list","watchlist");
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
                        mav.addObject("listDetails",watchlistDetails);
                        mav.addObject("moovieList",watchlistDetails.getCard());
                        mav.addObject("mediaList",watchlistDetails.getContent());
                        mav.addObject("watchedCount",watchlistDetails.getCard().getCurrentUserWatchAmount());
                        mav.addObject("watchedListId",moovieListService.getMoovieListCards("Watched",userService.getInfoOfMyUser().getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),null,null,1,0).get(0).getMoovieListId());
                        mav.addObject("listCount",watchlistDetails.getCard().getSize());
                        mav.addObject("numberOfPages",numberOfPages);
                        mav.addObject("currentPage",pageNumber - 1);
                        mav.addObject("listOwner",watchlistDetails.getCard().getUsername());
                        mav.addObject("orderBy", orderBy);
                        mav.addObject("order", order);
                        mav.addObject("publicType",MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());

                        break;
                    case "liked-lists":
                        mav.addObject("showLists",moovieListService.getLikedMoovieListCards(requestedProfile.getUserId(), MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
                        listCount = userService.getLikedMoovieListCountForUser(username);
                        queries.put("list","liked-lists");
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                        //Obtener la cantidad de listas likeadas por el usuario
                        break;
                    case "reviews":
                        mav.addObject("reviewsList",reviewService.getMovieReviewsFromUser(requestedProfile.getUserId(),PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
                        queries.put("list","reviews");
                        listCount = requestedProfile.getReviewsCount();
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
                        //Obtener la cantidad de reviews del usuario
                        break;
                    case "followed":
                        mav.addObject("showLists",moovieListService.getFollowedMoovieListCards(requestedProfile.getUserId(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(),pageNumber-1));
                        queries.put("list","followed");
                        listCount = moovieListService.getFollowedMoovieListCardsCount(requestedProfile.getUserId(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                        break;
                    case "user-private-lists":
                        mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType(),null,null, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
                        queries.put("list","user-private-lists");
                        listCount = requestedProfile.getMoovieListCount();
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                        break;
                    default: // este es el caso para user-lists. como es el default al entrar al profile
                        mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),null,null, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
                        queries.put("list","user-lists");
                        listCount = requestedProfile.getMoovieListCount();
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                        //Obtener la cantidad de listas creadas por el usuario
                        break;
                }
            }else{
                mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),null,null, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
                queries.put("list","user-lists");
                listCount = requestedProfile.getMoovieListCount();
                numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                //Obtener la cantidad de listas creadas por el usuario
            }

            queries.put("orderBy",orderBy);
            queries.put("order",order);
            mav.addObject("numberOfPages",numberOfPages);
            mav.addObject("currentPage",pageNumber - 1);

            String urlBase = UriComponentsBuilder.newInstance().path("/profile/{username}").query("list={list}&orderBy={orderBy}&order={order}").buildAndExpand(queries).toUriString();
            mav.addObject("urlBase", urlBase);

            LOGGER.info("Returned user with username: {} for /profile.", username);
            return mav;

        }catch (UnableToFindUserException e){
            LOGGER.info("Failed to return user with username: {} for /profile.", username);
            return new ModelAndView("errors/404");
        }

    }

    @RequestMapping("/milkyLeaderboard")
    public ModelAndView profilePage(@RequestParam(value = "page",defaultValue = "1") final int pageNumber){
        ModelAndView mav = new ModelAndView("main/milkyPointsLeaderboard");
        mav.addObject("profiles", userService.getMilkyPointsLeaders(PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize(), pageNumber-1));

        int usersCount = userService.getUserCount();
        int pagesSize= PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize();
        int numberOfPages = (int) Math.ceil(usersCount * 1.0 / pagesSize);
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        mav.addObject("currentPage", pageNumber-1);
        mav.addObject("numberOfPages",numberOfPages);

        mav.addObject("urlBase", UriComponentsBuilder.newInstance().path("/milkyLeaderboard").toUriString());
        return mav;
    }

    @ControllerAdvice
    public static class FileUploadExceptionAdvice {
        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public String handleMaxSizeException(
                MaxUploadSizeExceededException exc,
                HttpServletRequest request,
                HttpServletResponse response) {
            String referer = request.getHeader("referer");
            return "redirect:" + referer + "?error=fileTooBig";
        }
    }

    @RequestMapping(value = "/profile/image/{username}", produces = "image/**")
    public @ResponseBody
    byte[] getProfilePicture(@PathVariable("username") final String username){
        return userService.getProfilePicture(username);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerForm(@Valid @ModelAttribute("registerForm") final RegisterForm form,
                                     final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        String token;
        try {
            token = userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (UnableToCreateUserException e) {
            return new ModelAndView("redirect:/register?error=" + e.getMessage());
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/register/sentEmail");
        modelAndView.addObject("token", token);
        return modelAndView;
    }

    @RequestMapping(value = "/register/sentEmail")
    public ModelAndView sentEmail() {
        LOGGER.info("Confirmation email was sent.");
        return new ModelAndView("main/sentEmail");
    }

    @RequestMapping(value = "/uploadProfilePicture", method = {RequestMethod.POST})
    public String uploadProfilePicture(@RequestParam("file") MultipartFile picture, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("referer");
        try {
            userService.setProfilePicture(picture);
        } catch (InvalidTypeException e) {
            redirectAttributes.addAttribute("error", messageSource.getMessage("user.fileNotAnImage",null, LocaleContextHolder.getLocale()));
        } catch (NoFileException e) {
            redirectAttributes.addAttribute("error", messageSource.getMessage("user.noFileChosen",null, LocaleContextHolder.getLocale()));
        } catch (FailedToSetProfilePictureException e) {
            redirectAttributes.addAttribute("error", messageSource.getMessage("user.updateImageFailure",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            // Handle other exceptions if needed
            redirectAttributes.addAttribute("error", messageSource.getMessage("user.error",null, LocaleContextHolder.getLocale()));
        }

        return "redirect:" + referer;
    }
}
