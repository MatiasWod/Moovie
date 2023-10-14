package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;

import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.servlet.http.HttpSession;
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


    private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping(value = "/register/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") final String token, RedirectAttributes redirectAttributes) {
        try {
            Token verificationToken = verificationTokenService.getToken(token).get();
            LOGGER.debug("Verification token: " + verificationToken.getToken());

            if (userService.confirmRegister(verificationToken)) {
                return new ModelAndView("redirect:/login");
            } else {
                redirectAttributes.addAttribute("token", token);
                redirectAttributes.addAttribute("message", "The verification token had expired. A new email was sent!");
                return new ModelAndView("redirect:/register/resendEmail");
            }
        } catch (VerificationTokenNotFoundException e) {
            return new ModelAndView("helloworld/404");
        }
    }


    //TODO
    @RequestMapping(value = "/register/tokentimedout")
    public ModelAndView tokenTimedOut(@RequestParam("token") final String token) {
        ModelAndView mav = new ModelAndView("helloworld/tokenTimedOut");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/register/resendEmail", method = RequestMethod.POST)
    public ModelAndView resendEmail(@RequestParam("token") final String token,
                                    @RequestParam(value = "message", required = false) final String message,
                                    RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("token", token);
        if (message == null || message.isEmpty()) {
            redirectAttributes.addAttribute("message", "Verification email has been resent successfully.");
        } else {
            redirectAttributes.addAttribute("message", message);
        }
        ModelAndView mav = new ModelAndView("redirect:/register/sentEmail");
        userService.resendVerificationEmail(token);
        return mav;
    }



    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String previousPage = request.getHeader("Referer");
        session.setAttribute("previousPage", previousPage);
        return new ModelAndView("helloworld/login");
    }


    @RequestMapping("/continueWithoutLogin")
    public ModelAndView continueWithoutLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String previousPage = (String) session.getAttribute("previousPage");
        return new ModelAndView("redirect:" + previousPage);
    }



    @RequestMapping("/profile/{username:.+}")
    public ModelAndView profilePage(@PathVariable String username,
                                    @RequestParam( value = "list", required = false) String list,
                                    @RequestParam(value = "page",defaultValue = "1") final int pageNumber){
        try{
            Profile requestedProfile = userService.getProfileByUsername(username);

            ModelAndView mav = new ModelAndView("helloworld/profile");

            int listCount = 0;
            int numberOfPages = 0;
            final Map<String, String> queries = new HashMap<>();
            queries.put("username",username);

            mav.addObject("profile",requestedProfile);
            mav.addObject("isMe",userService.isUsernameMe(username));

            if (list != null){
                switch (list) {
                    case "watched-list":
                        MoovieListDetails watchedDetails = moovieListService.getMoovieListDetails( -1 , "WATCHED" , username,null, "asc",PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),pageNumber-1);
                        listCount = watchedDetails.getCard().getSize();
                        queries.put("list","watched-list");
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
                        mav.addObject("listDetails",watchedDetails);
                        break;
                    case "watchlist":
                        MoovieListDetails watchlistDetails = moovieListService.getMoovieListDetails(-1, "WATCHLIST" , username, null, "asc",PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize(),pageNumber-1);
                        listCount = watchlistDetails.getCard().getSize();
                        queries.put("list","watchlist");
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize());
                        mav.addObject("listDetails",watchlistDetails);
                        break;
                    case "liked-lists":
                        mav.addObject("showLists",moovieListService.getLikedMoovieListCards(requestedProfile.getUserId(), MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(),pageNumber - 1));
                        listCount = requestedProfile.getLikedMoovieListCount();
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
                    default: // este es el caso para user-lists. como es el default al entrar al profile
                        mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
                        queries.put("list","user-lists");
                        listCount = requestedProfile.getMoovieListCount();
                        numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                        //Obtener la cantidad de listas creadas por el usuario
                        break;
                }
            }else{
                mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(), PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1));
                queries.put("list","user-lists");
                listCount = requestedProfile.getMoovieListCount();
                numberOfPages = (int) Math.ceil(listCount * 1.0 / PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize());
                //Obtener la cantidad de listas creadas por el usuario
            }


            mav.addObject("numberOfPages",numberOfPages);
            mav.addObject("currentPage",pageNumber - 1);

            String urlBase = UriComponentsBuilder.newInstance().path("/profile/{username}").query("list={list}").buildAndExpand(queries).toUriString();
            mav.addObject("urlBase", urlBase);

            return mav;

        }catch (UnableToFindUserException e){
            return new ModelAndView("helloworld/404");
        }

    }

    @ControllerAdvice
    public class FileUploadExceptionAdvice {
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
        return new ModelAndView("helloworld/sentEmail");
    }

    @RequestMapping(value = "/uploadProfilePicture", method = {RequestMethod.POST})
    public String uploadProfilePicture(@RequestParam("file") MultipartFile picture, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("referer");

        try {
            userService.setProfilePicture(picture);
        } catch (InvalidTypeException e) {
            redirectAttributes.addAttribute("error", "invalidType");
        } catch (NoFileException e) {
            redirectAttributes.addAttribute("error", "noFile");
        } catch (FailedToSetProfilePictureException e) {
            redirectAttributes.addAttribute("error", "failedSetProfilePicture");
        } catch (Exception e) {
            // Handle other exceptions if needed
            redirectAttributes.addAttribute("error", "error");
        }

        return "redirect:" + referer;
    }


}
