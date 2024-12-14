package ar.edu.itba.paw.webapp.dto.in;

public class ReportCreateDTO {
    String content;
    String reportedBy;
    int type;

    public ReportCreateDTO() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
