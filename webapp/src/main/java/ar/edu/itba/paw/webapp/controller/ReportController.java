package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Reports.*;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/reports")
@Component
public class ReportController {
    private final ReportService reportService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReports(@QueryParam("typeContent") String contentType) {
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

            return Response.ok(reportDTOs).build();
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
    }
}
