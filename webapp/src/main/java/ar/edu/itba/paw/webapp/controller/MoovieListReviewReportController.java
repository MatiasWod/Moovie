package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewReportDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.function.Supplier;

@Path("moovieListReviewReports")
public class MoovieListReviewReportController {
    private final ReportService reportService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    Logger logger = LoggerFactory.getLogger(MoovieListReviewReportController.class);

    @Autowired
    public MoovieListReviewReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GET
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_REPORT_LIST)
    public Response getReports(
            @QueryParam("reportType") Integer reportType,
            @QueryParam("moovieListReviewId") Integer moovieListReviewId,
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

            List<MoovieListReviewReport> reports;
            int totalCount;

            if (reportType != null) {
                reports = reportService.getMoovieListReviewReports(reportType, moovieListReviewId, pageSizeQuery, pageNumber);
                totalCount = reportService.getReportsCount(ResourceTypesEnum.MOOVIELIST_REVIEW.getDescription(), reportType, moovieListReviewId);
            } else {
                if (moovieListReviewId != null) {
                    reports = reportService.getMoovieListReviewReports(null, moovieListReviewId, pageSizeQuery, pageNumber);
                    totalCount = reportService.getReportsCount(ResourceTypesEnum.MOOVIELIST_REVIEW.getDescription(), null, moovieListReviewId);
                }else{
                    reports = reportService.getMoovieListReviewReports(pageSizeQuery, pageNumber);
                    totalCount = reportService.getReportsCount(ResourceTypesEnum.MOOVIELIST_REVIEW.getDescription());
                }

            }

            List<MoovieListReviewReportDto> reportDTOs = MoovieListReviewReportDto.fromMoovieListReviewReportList(reports, uriInfo);

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListReviewReportDto>>(reportDTOs) {
            });
            final PagingUtils<MoovieListReviewReportDto> pagingUtils = new PagingUtils<>(reportDTOs, pageNumber, pageSizeQuery,
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
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_REPORT)
    public Response report(
            @Valid final ReportCreateDto reportDto) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            MoovieListReviewReport response = reportService.reportMoovieListReview(
                    reportDto.getResourceId(),
                    currentUser.getUserId(),
                    reportDto.getType());
            return Response.ok(MoovieListReviewReportDto.fromMoovieListReviewReport(response, uriInfo)).build();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @GET
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_REPORT)
    public Response getReport(@PathParam("id") @NotNull int id, @Context Request request) {
        try {
            MoovieListReviewReport report = reportService.getMoovieListReviewReport(id);
            final Supplier<MoovieListReviewReportDto> dtoSupplier = () -> MoovieListReviewReportDto.fromMoovieListReviewReport(report, uriInfo);
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
            MoovieListReviewReport report = reportService.getMoovieListReviewReport(id);
            reportService.resolveMoovieListReviewReport(id);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }
}
