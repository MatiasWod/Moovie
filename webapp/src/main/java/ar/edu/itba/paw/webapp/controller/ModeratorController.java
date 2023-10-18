package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.services.ModeratorService;
import ar.edu.itba.paw.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId, @RequestParam("path") String path, RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            moderatorService.deleteReview(reviewId, mediaId);
            redirectAttributes.addFlashAttribute("successMessage", "Review successfully deleted");
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting review");
        }
        return new ModelAndView("redirect:" + path);
    }

    @RequestMapping(value = "/deleteList/{moovieListId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieList(@PathVariable int moovieListId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.deleteMoovieListList(moovieListId);
            redirectAttributes.addFlashAttribute("successMessage", "Moovie list deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting Moovie list");
            return new ModelAndView("redirect:/list/" + moovieListId);
        }
        return new ModelAndView("redirect:/lists");
    }



    @RequestMapping(value = "/banUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView banUser(@PathVariable int userId, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "message", required = false) String message ) {

        try {
            moderatorService.banUser(userId, message);
            redirectAttributes.addFlashAttribute("successMessage", "User successfully banned");
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404").addObject("extrainfo", "Error banning user, cant find user");
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error banning user");
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
    }

    @RequestMapping(value = "/unbanUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView unbanUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.unbanUser(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User successfully unbanned");
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404").addObject("extrainfo", "Error unbanning user, cant find user");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error unbanning user");
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());

    }


    @RequestMapping(value = "/makeUserMod/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView makeUserMod(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.makeUserModerator(userId);
            redirectAttributes.addFlashAttribute("successMessage", "User successfully promoted to moderator");
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error promoting user to moderator");
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
    }
}
