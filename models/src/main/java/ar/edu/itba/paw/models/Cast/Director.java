package ar.edu.itba.paw.models.Cast;

public class Director {
    private int directorId;

    private String name;

    private int totalMedia;

    public Director(int directorId, String name, int totalMedia) {
        this.totalMedia = totalMedia;
        this.directorId = directorId;
        this.name = name;
    }

    public int getTotalMedia() {
        return totalMedia;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getName() {
        return name;
    }
}
