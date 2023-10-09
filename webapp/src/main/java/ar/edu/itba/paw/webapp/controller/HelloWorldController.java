package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;

import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListDetails;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;



@Controller
public class HelloWorldController {

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
    public ModelAndView confirmRegistration(@RequestParam("token") final String token) {
        Token verificationToken = verificationTokenService.getToken(token).orElseThrow(VerificationTokenNotFoundException::new);
        LOGGER.debug("Verification token: " + verificationToken.getToken());
        if(userService.confirmRegister(verificationToken)) {
            return new ModelAndView("redirect:/login");

        }
       //TODO return new ModelAndView("redirect:/register/tokentimedout?token=" + token);
        return new ModelAndView("redirect:/register/tokentimedout?token=" + token);
    }

    //TODO
    @RequestMapping(value = "/register/tokentimedout")
    public ModelAndView tokenTimedOut(@RequestParam("token") final String token) {
        ModelAndView mav = new ModelAndView("helloworld/tokenTimedOut");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/register/resendemail")
    public ModelAndView resendEmail(@RequestParam("token") final String token) {
        ModelAndView mav = new ModelAndView("helloworld/sentEmail");
        userService.resendVerificationEmail(token);
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("helloworld/login");
    }


    @RequestMapping("/profile/{username:.+}")
    public ModelAndView profilePage(@PathVariable String username,
                                    @RequestParam( value = "list", required = false) String list,
                                    @RequestParam(value = "page",defaultValue = "1") final int pageNumber){
        try{
            Profile requestedProfile = userService.getProfileByUsername(username);

            ModelAndView mav = new ModelAndView("helloworld/profile");

            int listCount = 0;
            int numberOfPages;

            mav.addObject("profile",requestedProfile);
            mav.addObject("isMe",userService.isUsernameMe(username));

            if (list != null){
                switch (list) {
                    case "watched-list":
                        MoovieListDetails watchedDetails = moovieListService.getMoovieListDetails( -1 , "WATCHED" , username,null, "asc",MoovieListService.DEFAULT_PAGE_SIZE_CONTENT,pageNumber-1);
                        listCount = watchedDetails.getContent().size();
                        mav.addObject("listDetails",watchedDetails);
                        break;
                    case "watchlist":
                        MoovieListDetails watchlistDetails = moovieListService.getMoovieListDetails(-1, "WATCHLIST" , username, null, "asc",MoovieListService.DEFAULT_PAGE_SIZE_CONTENT,pageNumber-1);
                        listCount = watchlistDetails.getContent().size();
                        mav.addObject("listDetails",watchlistDetails);
                        break;
                    case "liked-lists":
                        mav.addObject("showLists",moovieListService.getLikedMoovieListCards(requestedProfile.getUserId(),moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC,MoovieListService.DEFAULT_PAGE_SIZE_CARDS,pageNumber - 1));
                        //Obtener la cantidad de listas likeadas por el usuario
                        break;
                    case "reviews":
                        mav.addObject("reviewsList",reviewService.getMovieReviewsFromUser(requestedProfile.getUserId()));
                        //Obtener la cantidad de reviews del usuario
                        break;
                    default: // este es el caso para user-lists. como es el default al entrar al profile
                        mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, moovieListService.DEFAULT_PAGE_SIZE_CARDS,pageNumber - 1));
                        //Obtener la cantidad de listas creadas por el usuario
                        break;
                }
            }else{
                mav.addObject("showLists", moovieListService.getMoovieListCards(null, requestedProfile.getUsername(),moovieListService.MOOVIE_LIST_TYPE_STANDARD_PUBLIC, moovieListService.DEFAULT_PAGE_SIZE_CARDS,0));
            }

            numberOfPages = (int) Math.ceil(listCount * 1.0 / moovieListService.DEFAULT_PAGE_SIZE_CONTENT);
            mav.addObject("numberOfPages",numberOfPages);
            mav.addObject("currentPage",pageNumber - 1);

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
    public ModelAndView registerForm(@Valid @ModelAttribute("registerForm") final RegisterForm form,final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        try{
            userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (UnableToCreateUserException e){
            return new ModelAndView("redirect:/register?error=" + e.getMessage());
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/uploadProfilePicture", method = {RequestMethod.POST})
    public String uploadProfilePicture(@RequestParam("file") MultipartFile picture, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        try {
            userService.setProfilePicture(picture);
        }catch (InvalidTypeException e) {
            return "redirect:" + referer + "?error=invalidType";
        } catch (NoFileException e) {
            return "redirect:" + referer + "?error=noFile";
        } catch (FailedToSetProfilePictureException e) {
            return "redirect:" + referer + "?error=failedSetProfilePicture";
        } catch (Exception e) {
            // Handle other exceptions if needed
            return "redirect:" + referer + "?error=error";
        }
        return "redirect:" + referer;
    }

}
