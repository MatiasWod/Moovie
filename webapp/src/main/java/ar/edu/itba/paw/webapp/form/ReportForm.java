package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportForm {


    @NotEmpty(message = "Report Type Required")
    private int reportType;


    private String type;

    private String content;

    private int reportedBy;

    private int id;

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public int getReportedBy() {
        return reportedBy;
    }

    public String getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReportedBy(int reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setType(String type) {
        this.type = type;
    }
}
