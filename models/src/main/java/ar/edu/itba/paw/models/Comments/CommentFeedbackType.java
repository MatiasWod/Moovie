package ar.edu.itba.paw.models.Comments;

public enum CommentFeedbackType {
    LIKE("feedback.like"),UNLIKE("feedback.unlike"),DISLIKE("feedback.dislike"),UNDISLIKE("feedback.undislike");

    private final String code;

    CommentFeedbackType(String code){
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

