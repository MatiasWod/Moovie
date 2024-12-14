package ar.edu.itba.paw.webapp.dto.in;

public class ReportCreateDTO {
    String content;
    int type;

    public ReportCreateDTO() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
