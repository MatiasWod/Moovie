package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.List;

public class MoovieListReviewReportDto {
    private String url;

    private int reportId;

    private int type;

    private LocalDateTime report_date;

    private String reportedByUrl;

    private String moovieListReviewUrl;


    public static MoovieListReviewReportDto fromMoovieListReviewReport(MoovieListReviewReport moovieListReviewReport, UriInfo uriInfo) {
        MoovieListReviewReportDto dto = new MoovieListReviewReportDto();
        dto.url = uriInfo.getBaseUriBuilder().path("moovieListReviewReports/{id}")
                .build(moovieListReviewReport.getReportId()).toString();
        dto.reportId = moovieListReviewReport.getReportId();
        dto.type = moovieListReviewReport.getType();
        dto.report_date = moovieListReviewReport.getReport_date();

        dto.reportedByUrl = uriInfo.getBaseUriBuilder().path("users/{username}")
                .build(moovieListReviewReport.getReportedBy().getUsername()).toString();
        dto.moovieListReviewUrl = uriInfo.getBaseUriBuilder().path("moovieListReviews/{id}")
                .build(moovieListReviewReport.getMoovieListReview().getMoovieListReviewId()).toString();

        return dto;
    }

    public static List<MoovieListReviewReportDto> fromMoovieListReviewReportList(List<MoovieListReviewReport> moovieListReviewReports, UriInfo uriInfo) {
        return moovieListReviewReports.stream().map(moovieListReviewReport -> fromMoovieListReviewReport(moovieListReviewReport, uriInfo)).collect(java.util.stream.Collectors.toList());
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
    public String getMoovieListReviewUrl() {
        return moovieListReviewUrl;
    }

    public void setMoovieListReviewUrl(String moovieListReviewUrl) {
        this.moovieListReviewUrl = moovieListReviewUrl;
    }
}
