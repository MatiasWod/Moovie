package ar.edu.itba.paw.webapp.dto.in;

import ar.edu.itba.paw.models.Review.ReviewFeedbackType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ReviewFeedbackDto {

    @Pattern(regexp ="^(LIKE|UNLIKE)$")
    @NotNull
    private String feedbackType;

    public ReviewFeedbackDto() {
    }

    public ReviewFeedbackDto(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public ReviewFeedbackType transformToEnum() {
        return ReviewFeedbackType.valueOf(feedbackType);
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }
}
