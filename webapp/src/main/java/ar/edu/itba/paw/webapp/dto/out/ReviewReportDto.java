package ar.edu.itba.paw.webapp.dto.out;


import ar.edu.itba.paw.models.Reports.ReviewReport;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewReportDto {

    private String url;

    private int reportId;

    private int type;

    private LocalDateTime report_date;

    private String reportedByUrl;

    private String reviewUrl;


    public static ReviewReportDto fromReviewReport(ReviewReport reviewReport, UriInfo uriInfo) {
        ReviewReportDto dto = new ReviewReportDto();
        dto.url = uriInfo.getBaseUriBuilder().path("reviewReports/{id}")
                .build(reviewReport.getReportId()).toString();
        dto.reportId = reviewReport.getReportId();
        dto.type = reviewReport.getType();
        dto.report_date = reviewReport.getReport_date();

        dto.reportedByUrl = uriInfo.getBaseUriBuilder().path("users/{username}")
                .build(reviewReport.getReportedBy().getUsername()).toString();
        dto.reviewUrl = uriInfo.getBaseUriBuilder().path("reviews/{id}")
                .build(reviewReport.getReview().getReviewId()).toString();

        return dto;
    }

    public static List<ReviewReportDto> fromReviewReportList(List<ReviewReport> reviewReports, UriInfo uriInfo) {
        return reviewReports.stream().map(mlc -> fromReviewReport(mlc, uriInfo)).collect(java.util.stream.Collectors.toList());
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

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
}
