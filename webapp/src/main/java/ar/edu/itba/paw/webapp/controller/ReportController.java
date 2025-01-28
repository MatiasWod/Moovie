package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/reports")
@Component
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReportController(ReportService reportService,UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReports(@QueryParam("contentType") String contentType) {
        try {
            // Fetch reports based on filters using the ReportService
            List<Object> reports = reportService.getReports(contentType);

            // Map reports to DTOs
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

            return Response.ok(new GenericEntity<List<ReportDTO>>(reportDTOs) {
            }).build();
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportReview(@QueryParam("commentId") final int commentId,
                                 @Valid final ReportCreateDTO reportDTO) {
        try {
            User currentUser = userService.getInfoOfMyUser();
            CommentReport response = reportService.reportComment(commentId, currentUser.getUserId(), reportDTO.getType(), reportDTO.getContent());
            return Response.ok(ReportDTO.fromCommentReport(response, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }

    }

    @DELETE
    public Response resolveReport(@QueryParam("commentId") final int commentId) {
        try {
            reportService.resolveCommentReport(commentId);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
        return Response.ok().build();
    }
}
