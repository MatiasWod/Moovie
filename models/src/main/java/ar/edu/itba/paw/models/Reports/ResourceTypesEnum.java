package ar.edu.itba.paw.models.Reports;

public enum ResourceTypesEnum {
    COMMENT(1,"comment"),
    REVIEW(2,"review"),
    MOOVIELIST(3,"moovieList"),
    MOOVIELIST_REVIEW(4,"moovieListReview");

    private final int type;
    private final String description;

    ResourceTypesEnum(int type,String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public int getType() {
        return type;
    }
}
