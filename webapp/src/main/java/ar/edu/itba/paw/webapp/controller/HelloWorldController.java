package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import ar.edu.itba.paw.models.MoovieList.extendedMoovieList;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.extendedReview;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

;

@Controller
public class HelloWorldController {

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    MoovieListService moovieListService;

    @Autowired
    MediaService mediaService;

    @Autowired
    ReviewService reviewService;


    @RequestMapping("/{id:\\d+}")
    public ModelAndView profile(@PathVariable("id") final long userId) {
        final ModelAndView mav = new ModelAndView("helloworld/profile");
        try{
            mav.addObject("user",userService.getInfoOfMyUser());
        }catch(UnableToFindUserException exception){
            mav.addObject("user",null);
        }
        mav.addObject("userid", userId);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("helloworld/register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerForm(@Valid @ModelAttribute("registerForm") final RegisterForm form,final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        try{
            userService.createUser(form.getUsername(), form.getEmail(), form.getPassword());
        } catch (UnableToCreateUserException e){
            return new ModelAndView("redirect:/register?error:" + e.getMessage());
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/register/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") final String token) {
        Token verificationToken = verificationTokenService.getToken(token).orElseThrow(VerificationTokenNotFoundException::new);
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


    @RequestMapping("/profile/{username}")
    public ModelAndView profilePage(@PathVariable String username){
        Optional<User> aux = userService.findUserByUsername(username);
        if(aux.isPresent()){

            ModelAndView mav = new ModelAndView("helloworld/profile");
            mav.addObject("user", aux.get() );
            User myUser = userService.getInfoOfMyUser();
            mav.addObject("myUser", myUser);

            Boolean owner = userService.isUsernameMe(username);
            mav.addObject("isMe", owner);
            if (owner){
                List<MoovieList> privateLists = moovieListService.getMoovieListDefaultPrivateFromCurrentUser();
                mav.addObject("privateLists", privateLists);
                privateLists.forEach(list -> {

                    int moovieListId = list.getMoovieListId();
                    String listName = list.getName();

                    boolean isLiked = moovieListService.likeMoovieListStatusForUser(moovieListId);
                    mav.addObject("isLiked" + listName, isLiked);
                    mav.addObject("moovieList" + listName, list);

                    List<Media> mediaList = mediaService.getMediaByMoovieListId(moovieListId, mediaService.DEFAULT_PAGE_SIZE, 0);
                    List<MoovieListContent> moovieListContent = moovieListService.getMoovieListContentById(moovieListId);
                    String listOwner = myUser.getUsername();
                    int likeCount = moovieListService.getLikesCount(moovieListId).get();
                    int moviesCount = moovieListService.getMoovieListSize(moovieListId, false).get();
                    int tvSeriesCount = moovieListService.getMoovieListSize(moovieListId, true).get();
                    List<MoovieListContent> watchedMovies = moovieListService.getMediaWatchedInMoovieList(moovieListId);
                    mav.addObject("mediaList" + listName, mediaList);
                    mav.addObject("moovieListContent" + listName, moovieListContent);
                    mav.addObject("listOwner" + listName, listOwner);
                    mav.addObject("likeCount" + listName, likeCount);
                    mav.addObject("moviesCount" + listName, moviesCount);
                    mav.addObject("tvSeriesCount" + listName, tvSeriesCount);
                    mav.addObject("watchedMovies" + listName, watchedMovies);
                    mav.addObject("watchedMoviesSize" + listName, watchedMovies.size());

                });
            }


            List<MoovieList> likedLists = moovieListService.likedMoovieListsForUser(aux.get().getUserId(),MoovieListService.DEFAULT_PAGE_SIZE,0);
            ArrayList<extendedMoovieList> finalLikedLists = new ArrayList<extendedMoovieList>();
            for (MoovieList movieList : likedLists) {
                List<Media> mediaList = mediaService.getMoovieListContentByIdMediaBUpTo(movieList.getMoovieListId(), 4);
                String[] posters = new String[mediaList.size()];
                for (int i = 0; i < mediaList.size(); i++) {
                    posters[i] = mediaList.get(i).getPosterPath();
                }
                finalLikedLists.add(new extendedMoovieList(movieList, userService.findUserById(movieList.getUserId()).get().getUsername(),
                        moovieListService.getLikesCount(movieList.getMoovieListId()).get()
                        , moovieListService.getMoovieListSize(movieList.getMoovieListId(), false).get(), moovieListService.getMoovieListSize(movieList.getMoovieListId(), true).get(), posters));

            }
            mav.addObject("likedLists", finalLikedLists);

            List<MoovieList> userLists = moovieListService.getAllStandardPublicMoovieListFromUser(aux.get().getUserId(),MoovieListService.DEFAULT_PAGE_SIZE,0);
            ArrayList<extendedMoovieList> finalUserLists = new ArrayList<extendedMoovieList>();
            for (MoovieList movieList : userLists) {
                List<Media> mediaList = mediaService.getMoovieListContentByIdMediaBUpTo(movieList.getMoovieListId(), 4);
                String[] posters = new String[mediaList.size()];
                for (int i = 0; i < mediaList.size(); i++) {
                    posters[i] = mediaList.get(i).getPosterPath();
                }
                finalUserLists.add(new extendedMoovieList(movieList, userService.findUserById(movieList.getUserId()).get().getUsername(),
                        moovieListService.getLikesCount(movieList.getMoovieListId()).get()
                        , moovieListService.getMoovieListSize(movieList.getMoovieListId(), false).get(), moovieListService.getMoovieListSize(movieList.getMoovieListId(), true).get(), posters));

            }
            mav.addObject("userLists",finalUserLists);

            final List<Review> reviewList = reviewService.getMovieReviewsFromUser(aux.get().getUserId());
            final List<extendedReview> notEmptyContentReviewList = new ArrayList<>();
            for (Review review : reviewList) {
                if (review.getReviewContent() != null && !review.getReviewContent().isEmpty()) {
//                    notEmptyContentReviewList.add(review);
                    Media auxMedia = mediaService.getMediaById(review.getMediaId()).get();
                    notEmptyContentReviewList.add(new extendedReview(review.getReviewId(),
                            review.getUserId(),review.getMediaId(),review.getRating(),
                            review.getReviewLikes(), review.getReviewContent(),
                            auxMedia.getPosterPath(), auxMedia.getName()));
                }
            }
            mav.addObject("notEmptyContentReviewList", notEmptyContentReviewList);




            return mav;
        }
        ModelAndView mav = new ModelAndView("helloworld/404");
        mav.addObject("extraInfo", "User " + username + " not found");
        return mav;
    }

    @RequestMapping(value = "/uploadProfilePicture", method = {RequestMethod.POST})
    public String uploadProfilePicture(@RequestParam("file") MultipartFile picture, HttpServletRequest request) {
        String referer = request.getHeader("referer");
        try {
            userService.setProfilePicture(picture);
        }catch (InvalidTypeException e) {
            return "redirect:" + referer + "?error:invalidType";
        } catch (NoFileException e) {
            return "redirect:" + referer + "?error:noFile";
        } catch (FailedToSetProfilePictureException e) {
            return "redirect:" + referer + "?error:failedSetProfilePicture";
        }
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/profile/image/{username}", produces = "image/**")
    public @ResponseBody
    byte[] getProfilePicture(@PathVariable("username") final String username){
        return userService.getProfilePicture(username);
    }

}
