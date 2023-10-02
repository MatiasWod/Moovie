package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.services.ModeratorService;
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

    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId, RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            moderatorService.deleteReview(reviewId);
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
        try {
            moderatorService.banUser(userId);
        }catch (Exception e){
        }
        return new ModelAndView("redirect:/");
    }
}
