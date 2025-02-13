package ar.edu.itba.paw.models.Review;

public enum ReviewFeedbackType {
    LIKE("feedback.like"),UNLIKE("feedback.unlike");

    private final String code;

    ReviewFeedbackType(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    @Override
    public String toString(){
        return code;
    }
}
