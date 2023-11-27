package ar.edu.itba.paw.models.Cast;

public class Director {
    private int directorId;

    private String name;

    public Director(int directorId, String name) {
        this.directorId = directorId;
        this.name = name;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getName() {
        return name;
    }
}
