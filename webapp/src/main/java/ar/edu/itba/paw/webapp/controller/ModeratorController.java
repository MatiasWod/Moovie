package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.ModeratorService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.ReportForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ar.edu.itba.paw.services.*;

import javax.validation.Valid;

@Controller
public class ModeratorController {
    @Autowired
    private ModeratorService moderatorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId, @RequestParam("path") String path, RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            moderatorService.deleteReview(reviewId, mediaId, ReviewTypes.REVIEW_MEDIA);
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

    // ---------- MODERATOR REPORT MANAGEMENT -------------- //


    @RequestMapping(value = "/reports/new")
    public ModelAndView report(@ModelAttribute("reportForm") final ReportForm form,
                               @RequestParam("id") int id,
                               @RequestParam("reportedBy") int reportedBy,
                               @RequestParam("type") String type) {

        ModelAndView mav = new ModelAndView("helloworld/reportPage");
        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        return mav;
    }

    @RequestMapping(value = "/reports/new", method = RequestMethod.POST)
    public ModelAndView submitReport(@ModelAttribute("reportForm") final ReportForm form,
                                     final BindingResult errors,
                                     @RequestParam("id") int id,
                                     @RequestParam("reportedBy") int reportedBy,
                                     @RequestParam("type") String type,
                                     RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return report(form,id,reportedBy,type);
        }

        switch (type) {
            case "reviewDetails":
                try {
                    reportService.reportReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", "Review Reported");
                    return new ModelAndView("redirect:/details/" + reviewService.getReviewById(form.getId()).getMediaId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to Report Review");
                    return report(form, id, reportedBy, type);
                }
            case "review":
                try {
                    reportService.reportReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", "Review Reported");
                    return new ModelAndView("redirect:/review/" + form.getId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to Report Review");
                    return report(form, id, reportedBy, type);
                }
            case "moovieList":
                try {
                    reportService.reportMoovieList(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", "Review Reported");
                    return new ModelAndView("redirect:/list/" + form.getId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to Report Review");
                    return report(form, id, reportedBy, type);
                }
            case "moovieListReview":
                try {
                    reportService.reportMoovieListReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", "Review Reported");
                    return new ModelAndView("redirect:/list/" + reviewService.getMoovieListReviewById(form.getId()).getMoovieListId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to Report Review");
                    return report(form, id, reportedBy, type);
                }
            case "comment":
                try {
                    reportService.reportComment(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", "Review Reported");
                    return new ModelAndView("redirect:/details/"); // faltaria un getCommentById
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Unable to Report Review");
                    return report(form, id, reportedBy, type);
                }
        }
        return new ModelAndView("redirect:/");
    }

    // TODO: Add mod filtering for this page
    @RequestMapping(value = "/reports/review")
    public ModelAndView reportReview(@RequestParam(name = "list", required = false) String list) {
        ModelAndView mav = new ModelAndView("helloworld/pendingReports");

        switch (list){
            case("comments"):
                mav.addObject("list",reportService.getReportedComments());
                break;
            case("reviews"):
                mav.addObject("list",reportService.getReviewReports());
                break;
            case("mlReviews"):
                mav.addObject("list",reportService.getReportedMoovieListReviews());
                break;
            case("ml"):
                mav.addObject("list",reportService.getReportedMoovieLists());
                break;
            default:
                mav.addObject("list",reportService.getReportedComments());
                break;

        }

        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }
        return mav;
    }
}
