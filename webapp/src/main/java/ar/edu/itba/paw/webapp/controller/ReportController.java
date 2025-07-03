package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/reports")
@Path("/reports")
@Component
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;
    private final CommentService commentService;
    private final MoovieListService moovieListService;
    private final ReviewService reviewService;

    @Context
    private UriInfo uriInfo;

    Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    public ReportController(ReportService reportService, UserService userService, CommentService commentService,
            MoovieListService moovieListService, ReviewService reviewService) {
        this.reportService = reportService;
        this.userService = userService;
        this.commentService = commentService;
        this.moovieListService = moovieListService;
        this.reviewService = reviewService;
    }

    @GET
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_REPORT_LIST)
    public Response getReports(@QueryParam("contentType") String contentType,
            @QueryParam("reportType") Integer reportType,
            @QueryParam("resourceId") Integer resourceId,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {


            if (contentType != null && !contentType.equalsIgnoreCase("comment") &&
                    !contentType.equalsIgnoreCase("moovieList") && !contentType.equalsIgnoreCase("moovieListReview") &&
                    !contentType.equalsIgnoreCase("review")) {
                throw new IllegalArgumentException(
                        "The 'contentType' query parameter must be one of 'comment', 'moovieList', 'moovieListReview', or 'review'.");
            }

            if (reportType != null && (reportType < ReportTypesEnum.hatefulContent.getType()
                    || reportType > ReportTypesEnum.spam.getType())) {
                throw new IllegalArgumentException(
                        "The 'reportType' query parameter must be between 0 and 3 (0=hatefulContent, 1=abuse, 2=privacy, 3=spam).");
            }

            if (resourceId != null && reportType == null && contentType == null) {
                throw new IllegalArgumentException(
                        "The 'contentType' query parameter must be provided when 'resourceId' is specified.");
            }

            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize();
            }

            List<Object> reports;
            int totalCount;

            if (reportType != null || resourceId != null) {
                reports = reportService.getReports(contentType, reportType, resourceId, pageSizeQuery, pageNumber);
                totalCount = reportService.getReportsCount(contentType, reportType, resourceId);
            } else {
                reports = reportService.getReports(contentType, pageSizeQuery, pageNumber);
                totalCount = reportService.getReportsCount(contentType);
            }

            List<ReportDTO> reportDTOs = reports.stream().map(report -> {
                if (report instanceof ReviewReport) {
                    return ReportDTO.fromReviewReport((ReviewReport) report, uriInfo);
                } else if (report instanceof CommentReport) {
                    return ReportDTO.fromCommentReport((CommentReport) report, uriInfo);
                } else if (report instanceof MoovieListReport) {
                    return ReportDTO.fromMoovieListReport((MoovieListReport) report, uriInfo);
                } else if (report instanceof MoovieListReviewReport) {
                    return ReportDTO.fromMoovieListReviewReport((MoovieListReviewReport) report, uriInfo);
                }
                return null;
            }).collect(Collectors.toList());

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOs) {
            });
            final PagingUtils<ReportDTO> pagingUtils = new PagingUtils<>(reportDTOs, pageNumber, pageSizeQuery,
                    totalCount);
            ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
            return res.build();
        } catch (Exception e) {
            logger.error(
                    "Getting reports:\n contentType: {}, reportType: {}, resourceId: {}, pageNumber: {}, pageSize: {}",
                    contentType, reportType, resourceId, pageNumber, pageSize);
            logger.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_REPORT_FORM)
    @Produces(VndType.APPLICATION_REPORT)
    public Response report(
            @QueryParam("commentId") final Integer commentId,
            @QueryParam("moovieListId") final Integer moovieListId,
            @QueryParam("moovieListReviewId") final Integer moovieListReviewId,
            @QueryParam("reviewId") final Integer reviewId,
            @Valid final ReportCreateDTO reportDTO) {
        try {
            User currentUser = userService.getInfoOfMyUser();

            if (commentId != null) {
                // Lógica para reportar un comentario
                CommentReport response = reportService.reportComment(
                        commentId,
                        currentUser.getUserId(),
                        reportDTO.getType()

                );
                return Response.ok(ReportDTO.fromCommentReport(response, uriInfo)).build();
            } else if (moovieListId != null) {
                // Lógica para reportar una lista de películas
                MoovieListReport response = reportService.reportMoovieList(
                        moovieListId,
                        currentUser.getUserId(),
                        reportDTO.getType()

                );
                return Response.ok(ReportDTO.fromMoovieListReport(response, uriInfo)).build();
            } else if (moovieListReviewId != null) {
                // Lógica para reportar una reseña de lista de películas
                MoovieListReviewReport response = reportService.reportMoovieListReview(
                        moovieListReviewId,
                        currentUser.getUserId(),
                        reportDTO.getType());
                return Response.ok(ReportDTO.fromMoovieListReviewReport(response, uriInfo)).build();
            } else if (reviewId != null) {
                // Lógica para reportar una reseña general
                ReviewReport response = reportService.reportReview(
                        reviewId,
                        currentUser.getUserId(),
                        reportDTO.getType());
                return Response.ok(ReportDTO.fromReviewReport(response, uriInfo)).build();
            } else {
                throw new IllegalArgumentException(
                        "At least one of 'commentId', 'reviewId', 'moovieListReviewId', or 'generalReviewId' must be provided.");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @DELETE
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveReport(
            @QueryParam("moovieListId") final Integer moovieListId,
            @QueryParam("commentId") final Integer commentId,
            @QueryParam("moovieListReviewId") final Integer moovieListReviewId,
            @QueryParam("reviewId") final Integer reviewId) {
        try {
            if (moovieListId != null) {
                // Resolver reporte de una lista de películas
                reportService.resolveMoovieListReport(moovieListId);
            } else if (commentId != null) {
                // Resolver reporte de un comentario
                reportService.resolveCommentReport(commentId);
            } else if (moovieListReviewId != null) {
                // Resolver reporte de una reseña de lista de películas
                reportService.resolveMoovieListReviewReport(moovieListReviewId);
            } else if (reviewId != null) {
                // Resolver reporte de una reseña general
                reportService.resolveReviewReport(reviewId);
            } else {
                throw new IllegalArgumentException(
                        "At least one of 'moovieListId', 'commentId', 'moovieListReviewId', or 'reviewId' must be provided.");
            }
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

}
