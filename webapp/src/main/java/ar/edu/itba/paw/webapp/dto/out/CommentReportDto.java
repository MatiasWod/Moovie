package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.CommentReport;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.List;

public class CommentReportDto {
    private String url;

    private int reportId;

    private int type;

    private LocalDateTime report_date;

    private String reportedByUrl;

    private String commentUrl;


    public static CommentReportDto fromCommentReport(CommentReport commentReport, UriInfo uriInfo) {
        CommentReportDto dto = new CommentReportDto();
        dto.url = uriInfo.getBaseUriBuilder().path("commentsReports/{id}")
                .build(commentReport.getReportId()).toString();
        dto.reportId = commentReport.getReportId();
        dto.type = commentReport.getType();
        dto.report_date = commentReport.getReport_date();

        dto.reportedByUrl = uriInfo.getBaseUriBuilder().path("users/{username}")
                .build(commentReport.getReportedBy().getUsername()).toString();
        dto.commentUrl = uriInfo.getBaseUriBuilder().path("comments/{id}")
                .build(commentReport.getComment().getCommentId()).toString();

        return dto;
    }

    public static List<CommentReportDto> fromCommentReportList(List<CommentReport> commentsReports, UriInfo uriInfo) {
        return commentsReports.stream().map(commentsReport -> fromCommentReport(commentsReport, uriInfo)).collect(java.util.stream.Collectors.toList());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getReport_date() {
        return report_date;
    }

    public void setReport_date(LocalDateTime report_date) {
        this.report_date = report_date;
    }

    public String getReportedByUrl() {
        return reportedByUrl;
    }

    public void setReportedByUrl(String reportedByUrl) {
        this.reportedByUrl = reportedByUrl;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }
}
