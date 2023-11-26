package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.ModeratorService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.form.ReportForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ar.edu.itba.paw.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BannedService bannedService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ModeratorController.class);


    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId, @RequestParam("path") String path, RedirectAttributes redirectAttributes, @PathVariable int mediaId) {
        try {
            moderatorService.deleteReview(reviewId, mediaId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:" + path);
    }

    @RequestMapping(value = "/deleteList/{moovieListId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieList(@PathVariable int moovieListId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.deleteMoovieListList(moovieListId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.moovieListDeletedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.moovieListDeletedFailure",null, LocaleContextHolder.getLocale()));
            return new ModelAndView("redirect:/list/" + moovieListId);
        }
        return new ModelAndView("redirect:/lists");
    }



    @RequestMapping(value = "/banUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView banUser(@PathVariable int userId, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "message", required = false) String message ) {

        try {
            moderatorService.banUser(userId, message);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.userBannedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404").addObject("extrainfo", messageSource.getMessage("moderator.userBannedNotFound",null, LocaleContextHolder.getLocale()));
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.userBannedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
    }

    @RequestMapping(value = "/unbanUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView unbanUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.unbanUser(userId);
            redirectAttributes.addFlashAttribute("successMessage",  messageSource.getMessage("moderator.userUnbannedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404").addObject("extrainfo", messageSource.getMessage("moderator.userUnbannedNotFound",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.userUnbannedFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());

    }


    @RequestMapping(value = "/makeUserMod/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView makeUserMod(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.makeUserModerator(userId);
            redirectAttributes.addFlashAttribute("successMessage",  messageSource.getMessage("moderator.promoteToModSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToFindUserException e) {
            return new ModelAndView("helloworld/404");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.promoteToModFailure",null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
    }

    // ---------- MODERATOR REPORT MANAGEMENT -------------- //


    @RequestMapping(value = "/reports/new")
    public ModelAndView report(@ModelAttribute("reportForm") final ReportForm form,
                               @RequestParam("id") int id,
                               @RequestParam("reportedBy") int reportedBy,
                               @RequestParam("type") String type,
                               RedirectAttributes redirectAttributes) {

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
            return report(form,id,reportedBy,type, redirectAttributes);
        }
        LOGGER.info("Hola este es el type: "+form.getType() + " reportedBy: " +form.getReportedBy() + " id: " + form.getId());
        switch (type) {
            case "reviewDetails,reviewDetails":
                try {
                    reportService.reportReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/details/" + reviewService.getReviewById(form.getId()).getMediaId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "review,review":
                try {
                    reportService.reportReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/review/" + form.getId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "moovieList,moovieList":
                try {
                    reportService.reportMoovieList(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.moovieListReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/list/" + form.getId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.moovieListReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "moovieListReview,moovieListReview":
                try {
                    reportService.reportMoovieListReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.moovieListReviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/list/" + reviewService.getMoovieListReviewById(form.getId()).getMoovieListId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.moovieListReviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "comment,comment":
                try {
                    reportService.reportComment(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/details/"); // faltaria un getCommentById
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
        }
        return report(form, id, reportedBy, type, redirectAttributes);
    }

    // TODO: Add mod filtering for this page
    @RequestMapping(value = "/reports/review")
    public ModelAndView reportReview(@RequestParam(name = "list", required = false) String list) {
        ModelAndView mav = new ModelAndView("helloworld/pendingReports");

        // -- LISTS --
        if (list != null && list != ""){
            switch (list){
                case("comments"):
                    mav.addObject("list",reportService.getReportedComments());
                    mav.addObject("listCount",reportService.getReportedCommentsCount());
                    break;
                case("reviews"):
                    mav.addObject("list",reportService.getReviewReports());
                    mav.addObject("listCount",reportService.getReportedReviewsCount());
                    break;
                case("mlReviews"):
                    mav.addObject("list",reportService.getReportedMoovieListReviews());
                    mav.addObject("listCount",reportService.getReportedMoovieListReviewsCount());

                    break;
                case("ml"):
                    mav.addObject("list",reportService.getReportedMoovieLists());
                    mav.addObject("listCount",reportService.getReportedMoovieListsCount());
                    break;
                case("banned"):
                    mav.addObject("list", bannedService.getBannedUsers());
                    mav.addObject("listCount",bannedService.getBannedCount());
                    break;
            }
        }else{
            mav.addObject("list",reportService.getReportedComments());
            mav.addObject("listCount",reportService.getReportedCommentsCount());
        }



        // -- CURRENT USER --

        try {
            mav.addObject("currentUser", userService.getInfoOfMyUser());
        } catch (Exception e) {
            // do nothing
        }

        // -- GLOBAL STATS --
        mav.addObject("totalReports", reportService.getTotalReports());
        mav.addObject("spamReports", reportService.getTypeReports(ReportTypesEnum.spam.getType()));
        mav.addObject("hateReports", reportService.getTypeReports(ReportTypesEnum.hatefulContent.getType()));
        mav.addObject("abuseReports", reportService.getTypeReports(ReportTypesEnum.abuse.getType()));
        mav.addObject("privacyReports", reportService.getTypeReports(ReportTypesEnum.privacy.getType()));
        mav.addObject("totalBanned", bannedService.getBannedCount());


        return mav;
    }
}
