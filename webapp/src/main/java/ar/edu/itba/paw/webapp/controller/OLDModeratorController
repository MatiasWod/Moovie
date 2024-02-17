package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Reports.ReportTypesEnum;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.form.ReportForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

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


    @RequestMapping(value = "/deleteComment/{commentId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteComment(@RequestParam("reviewId") int reviewId,
                                      RedirectAttributes redirectAttributes,
                                      @PathVariable int commentId,
                                      HttpServletRequest request) {
        try {
            commentService.deleteComment(commentId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }

        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + reviewId);
        }  else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=comments");
        } else if (referer.contains("review")) {
            return new ModelAndView("redirect:/review/" + reviewId);
        }
        return new ModelAndView("redirect:" + "/details/" + reviewId);
    }

    @RequestMapping(value = "/deleteReview/{mediaId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam("reviewId") int reviewId,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable int mediaId,
                                     HttpServletRequest request) {
        try {
            moderatorService.deleteReview(reviewId, mediaId, ReviewTypes.REVIEW_MEDIA);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){

            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("details")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        }  else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=comments");
        } else if (referer.contains("review")) {
            return new ModelAndView("redirect:/details/" + mediaId);
        }
        return new ModelAndView("redirect:" + "/details/" + mediaId);
    }

    @RequestMapping(value = "/deleteList/{moovieListId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteMoovieList(@PathVariable int moovieListId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            moderatorService.deleteMoovieListList(moovieListId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.moovieListDeletedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.moovieListDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("list")) {
            return new ModelAndView("redirect:/lists");
        }  else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=ml");
        }
        return new ModelAndView("redirect:/lists");
    }



    @RequestMapping(value = "/deleteUserMoovieListReviewMod/{moovieListReviewId:\\d+}", method = RequestMethod.POST)
    public ModelAndView deleteUserMoovieListReviewMod(@RequestParam("reviewId") int mediaId,
                                               RedirectAttributes redirectAttributes,
                                               @PathVariable int moovieListReviewId,
                                               HttpServletRequest request) {
        try {
            moderatorService.deleteListReview(moovieListReviewId);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("list.reviewDeletedSuccess",null, LocaleContextHolder.getLocale()));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("list.reviewDeletedFailure",null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader("Referer");
        if (referer.contains("list")) {
            return new ModelAndView("redirect:/list/" + mediaId);
        } else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review?list=mlReviews");
        }
        return new ModelAndView("redirect:/list/" + mediaId);
    }


    @RequestMapping(value = "/banUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView banUser(@PathVariable int userId, RedirectAttributes redirectAttributes,
                                @RequestParam(value = "message", required = false) String message,
                                HttpServletRequest request) {

        try {
            moderatorService.banUser(userId, message);
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.userBannedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToFindUserException e) {
            return new ModelAndView("errors/404").addObject("extrainfo", messageSource.getMessage("moderator.userBannedNotFound",null, LocaleContextHolder.getLocale()));
        }  catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.userBannedFailure",null, LocaleContextHolder.getLocale()));
        }

        String referer = request.getHeader("Referer");
        if (referer.contains("profile")) {
            return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
        } else if (referer.contains("reports")) {
            return new ModelAndView("redirect:/reports/review");
        }
        return new ModelAndView("redirect:/profile/" + userService.findUserById(userId).getUsername());
    }

    @RequestMapping(value = "/unbanUser/{userId:\\d+}", method = RequestMethod.POST)
    public ModelAndView unbanUser(@PathVariable int userId, RedirectAttributes redirectAttributes) {
        try {
            moderatorService.unbanUser(userId);
            redirectAttributes.addFlashAttribute("successMessage",  messageSource.getMessage("moderator.userUnbannedSuccess",null, LocaleContextHolder.getLocale()));
        } catch (UnableToFindUserException e) {
            return new ModelAndView("errors/404").addObject("extrainfo", messageSource.getMessage("moderator.userUnbannedNotFound",null, LocaleContextHolder.getLocale()));
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
            return new ModelAndView("errors/404");
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

        ModelAndView mav = new ModelAndView("main/reportPage");
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
            case "details,details":
                try {
                    reportService.reportReview(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/details/" + reviewService.getReviewById(form.getId()).getMediaId());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "reviews,reviews":
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
                    return new ModelAndView("redirect:/review/" + commentService.getCommentById(form.getId()).getMediaId() + "/" + commentService.getCommentById(form.getId()).getReviewId()); // faltaria un getCommentById
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return report(form, id, reportedBy, type, redirectAttributes);
                }
            case "reviewComment,reviewComment":
                try {
                    reportService.reportComment(form.getId(), form.getReportedBy(), form.getReportType(), form.getContent());
                    redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("moderator.reviewReportedSuccess",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/details/" + commentService.getCommentById(form.getId()).getMediaId()); // faltaria un getCommentById

                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("moderator.reviewReportedFailure",null, LocaleContextHolder.getLocale()));
                    return new ModelAndView("redirect:/details/" + commentService.getCommentById(form.getId()).getMediaId()); // faltaria un getCommentById
                }
        }
        return report(form, id, reportedBy, type, redirectAttributes);
    }

    @RequestMapping(value = "/reports/resolve", method = RequestMethod.POST)
    public ModelAndView resolveReport(@RequestParam(name ="type", required = true) String type,
                                      @RequestParam(name = "id", required = true) int id){

        try{
            User currentUser = userService.getInfoOfMyUser();
            if (currentUser != null && currentUser.getRole() == 2){
                switch (type){
                    case("comments"):
                        reportService.resolveCommentReport(id);
                        break;
                    case("reviews"):
                        reportService.resolveReviewReport(id);
                        break;
                    case("ml"):
                        reportService.resolveMoovieListReport(id);
                        break;
                    case("mlReviews"):
                        reportService.resolveMoovieListReviewReport(id);
                        break;
                }
            }
        }catch(Exception e){
            return new ModelAndView("errors/404");
        }

        return reportReview(type);
    }


    @RequestMapping(value = "/reports/review")
    public ModelAndView reportReview(@RequestParam(name = "list", required = false) String list) {
        ModelAndView mav = new ModelAndView("user/pendingReports");

        // -- LISTS --
        if (list != null && list != ""){
            switch (list){
                case("comments"):
                    mav.addObject("commentList",reportService.getReportedComments());
                    mav.addObject("listCount",reportService.getReportedCommentsCount());
                    break;
                case("reviews"):
                    mav.addObject("reviewList",reportService.getReportedReviews());
                    mav.addObject("listCount",reportService.getReportedReviewsCount());
                    break;
                case("mlReviews"):
                    mav.addObject("mlReviewList",reportService.getReportedMoovieListReviews());
                    mav.addObject("listCount",reportService.getReportedMoovieListReviewsCount());

                    break;
                case("ml"):
                    mav.addObject("mlList",reportService.getReportedMoovieLists());
                    mav.addObject("listCount",reportService.getReportedMoovieListsCount());
                    break;
                case("banned"):
                    mav.addObject("bannedList", bannedService.getBannedUsers());
                    mav.addObject("listCount",bannedService.getBannedCount());
                    break;
                default:
                    return new ModelAndView("errors/404").addObject("extrainfo", "Illegal parameter");
            }
        }else{
            mav.addObject("commentList",reportService.getReportedComments());
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
