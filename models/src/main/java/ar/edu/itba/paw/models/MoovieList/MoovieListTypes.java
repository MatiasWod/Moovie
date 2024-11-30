package ar.edu.itba.paw.models.MoovieList;

public enum MoovieListTypes {
    MOOVIE_LIST_TYPE_STANDARD_PUBLIC(1),
    MOOVIE_LIST_TYPE_STANDARD_PRIVATE(2),
    MOOVIE_LIST_TYPE_DEFAULT_PUBLIC(3),
    MOOVIE_LIST_TYPE_DEFAULT_PRIVATE(4);

    private final int type;
    MoovieListTypes(int size) {
        this.type = size;
    }

    public int getType() {
        return type;
    }
}

