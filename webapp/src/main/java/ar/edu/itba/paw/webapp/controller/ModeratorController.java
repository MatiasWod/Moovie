package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.services.ModeratorService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ModeratorController {
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId, RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            moderatorService.deleteReview(reviewId, mediaId);
            redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully");
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting review");
        }
        return new ModelAndView("redirect:/details/" + mediaId);
    }

    @RequestMapping(value = "/deleteList/{moovieListId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieList(@PathVariable int moovieListId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.deleteMoovieListList(moovieListId);
        }catch (Exception e){
        }
        return new ModelAndView("redirect:/lists");
    }

    @RequestMapping(value = "/banUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView banUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        User bannedUser;
        try {
            bannedUser = userService.findUserById(userId);
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404");
        }
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/")
                .append(bannedUser.getUsername())
                .append("?");
        try {
            moderatorService.banUser(userId);
            redirectUrl.append("success=ban");
        } catch (Exception e) {
            redirectUrl.append("error=ban");
        }
        return new ModelAndView(redirectUrl.toString());
    }

    @RequestMapping(value = "/unbanUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView unbanUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        User bannedUser;
        try {
            bannedUser = userService.findUserById(userId);
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404");
        }
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/")
                .append(bannedUser.getUsername())
                .append("?");
        try {
            moderatorService.unbanUser(userId);
            redirectUrl.append("success=unban");
        } catch (Exception e) {
            redirectUrl.append("error=unban");
        }
        return new ModelAndView(redirectUrl.toString());
    }

    @RequestMapping(value = "/makeUserMod/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView makeUserMod(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        User user;
        try {
            user = userService.findUserById(userId);
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404");
        }
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/")
                .append(user.getUsername())
                .append("?");
        try {
            moderatorService.makeUserModerator(userId);
            redirectUrl.append("success=mod");
        } catch (Exception e) {
            redirectUrl.append("error=mod");
        }
        return new ModelAndView(redirectUrl.toString());
    }
}
