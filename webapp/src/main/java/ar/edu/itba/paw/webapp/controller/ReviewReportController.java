package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewReportDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewReportDto;
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
import java.util.function.Supplier;

@Api(value = "/reviewReports")
@Path("reviewReports")
@Component
public class ReviewReportController {
    private final ReportService reportService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    Logger logger = LoggerFactory.getLogger(ReviewReportController.class);

    @Autowired
    public ReviewReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GET
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_REVIEW_REPORT_LIST)
    public Response getReports(
            @QueryParam("reportType") Integer reportType,
            @QueryParam("reviewId") Integer reviewId,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {

            if (reportType != null && (reportType < ReportTypesEnum.HATEFUL_CONTENT.getType()
                    || reportType > ReportTypesEnum.SPAM.getType())) {
                throw new IllegalArgumentException(
                        "The 'reportType' query parameter must be between 0 and 3 (0=hatefulContent, 1=abuse, 2=privacy, 3=spam).");
            }

            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize();
            }

            List<ReviewReport> reports;
            int totalCount;

            if (reportType != null) {
                reports = reportService.getReviewReports(reportType, reviewId, pageSizeQuery, pageNumber);
                totalCount = reportService.getReportsCount(ResourceTypesEnum.REVIEW.getDescription(), reportType, reviewId);
            } else {
                if (reviewId != null) {
                    reports = reportService.getReviewReports(null, reviewId, pageSizeQuery, pageNumber);
                    totalCount = reportService.getReportsCount(ResourceTypesEnum.REVIEW.getDescription(), null, reviewId);
                }else{
                    reports = reportService.getReviewReports(pageSizeQuery, pageNumber);
                    totalCount = reportService.getReportsCount(ResourceTypesEnum.REVIEW.getDescription());
                }

            }

            List<ReviewReportDto> reportDTOs = ReviewReportDto.fromReviewReportList(reports, uriInfo);

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewReportDto>>(reportDTOs) {
            });
            final PagingUtils<ReviewReportDto> pagingUtils = new PagingUtils<>(reportDTOs, pageNumber, pageSizeQuery,
                    totalCount);
            ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
            return res.build();
        } catch (Exception e) {
            logger.error(
                    "Getting reports:\n  reportType: {}, pageNumber: {}, pageSize: {}",
                    reportType, pageNumber, pageSize);
            logger.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_REPORT_FORM)
    @Produces(VndType.APPLICATION_REVIEW_REPORT)
    public Response report(
            @Valid final ReportCreateDto reportDto) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            ReviewReport response = reportService.reportReview(
                    reportDto.getResourceId(),
                    currentUser.getUserId(),
                    reportDto.getType());
            return Response.ok(ReviewReportDto.fromReviewReport(response, uriInfo)).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @GET
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_REVIEW_REPORT)
    public Response getReport(@PathParam("id") @NotNull int id, @Context Request request) {
        try {
            ReviewReport report = reportService.getReviewReport(id);
            final Supplier<ReviewReportDto> dtoSupplier = () -> ReviewReportDto.fromReviewReport(report, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, report.hashCode());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resolveReport(@PathParam("id") @NotNull int id) {
        try {
            ReviewReport report = reportService.getReviewReport(id);
            reportService.resolveReviewReport(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

}
