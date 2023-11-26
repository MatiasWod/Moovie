package ar.edu.itba.paw.models.Reports;

import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.User.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "reportsMoovieListReviews")
public class MoovieListReviewReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reportid")
    private int reportId;

    @Column(name = "type", columnDefinition = "VARCHAR(50)", nullable = false)
    private String type;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "reportedBy", referencedColumnName = "userId")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "moovieListReviewId", referencedColumnName = "moovieListReviewId")
    private MoovieListReview moovieListReview;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime report_date;

    @Column(name = "resolved", nullable = false)
    private Boolean resolved;


    MoovieListReviewReport(){}

    public MoovieListReviewReport(String type, String content, User reportedBy, MoovieListReview mlr)
    {
        this.moovieListReview = mlr;
        this.resolved = false;
        this.reportedBy = reportedBy;
        this.content = content;
        this.type = type;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setMoovieListReview(MoovieListReview moovieListReview) {
        this.moovieListReview = moovieListReview;
    }

    public void setReport_date(LocalDateTime report_date) {
        this.report_date = report_date;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public int getReportId() {
        return reportId;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public MoovieListReview getMoovieListReview() {
        return moovieListReview;
    }

    public LocalDateTime getReport_date() {
        return report_date;
    }

    public Boolean getResolved() {
        return resolved;
    }
}
