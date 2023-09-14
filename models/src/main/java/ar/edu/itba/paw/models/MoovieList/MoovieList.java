package ar.edu.itba.paw.models.MoovieList;

public class MoovieList {
    private final int moovieListId;
    private final int userId;
    private final String name;
    private final String description;

    public MoovieList(int moovieListId, int userId, String name, String description) {
        this.moovieListId = moovieListId;
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
