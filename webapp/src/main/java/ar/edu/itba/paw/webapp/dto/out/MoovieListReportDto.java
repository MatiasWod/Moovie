package ar.edu.itba.paw.webapp.dto.out;

import ar.edu.itba.paw.models.Reports.MoovieListReport;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.List;

public class MoovieListReportDto {
    private String url;

    private int reportId;

    private int type;

    private LocalDateTime report_date;

    private String reportedByUrl;

    private String moovieListUrl;


    public static MoovieListReportDto fromMoovieListReport(MoovieListReport moovieListReport, UriInfo uriInfo) {
        MoovieListReportDto dto = new MoovieListReportDto();
        dto.url = uriInfo.getBaseUriBuilder().path("listReports/{id}")
                .build(moovieListReport.getReportId()).toString();
        dto.reportId = moovieListReport.getReportId();
        dto.type = moovieListReport.getType();
        dto.report_date = moovieListReport.getReport_date();

        dto.reportedByUrl = uriInfo.getBaseUriBuilder().path("users/{username}")
                .build(moovieListReport.getReportedBy().getUsername()).toString();
        dto.moovieListUrl = uriInfo.getBaseUriBuilder().path("lists/{id}")
                .build(moovieListReport.getMoovieList().getMoovieListId()).toString();

        return dto;
    }

    public static List<MoovieListReportDto> fromMoovieListReportList(List<MoovieListReport> moovieListReports, UriInfo uriInfo) {
        return moovieListReports.stream().map(moovieListReport -> fromMoovieListReport(moovieListReport, uriInfo)).collect(java.util.stream.Collectors.toList());
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

    public String getMoovieListUrl() {
        return moovieListUrl;
    }

    public void setMoovieListUrl(String moovieListUrl) {
        this.moovieListUrl = moovieListUrl;
    }
}
