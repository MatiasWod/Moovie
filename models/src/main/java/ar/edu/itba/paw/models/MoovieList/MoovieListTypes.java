package ar.edu.itba.paw.models.MoovieList;

public enum MoovieListTypes {
    MOOVIE_LIST_TYPE_STANDARD_PUBLIC(1), //Listas que crea un usuario y son publicas
    MOOVIE_LIST_TYPE_STANDARD_PRIVATE(2), //Listas que crea un usuario y puso privada
    MOOVIE_LIST_TYPE_DEFAULT_PUBLIC(3), //Listas creadas automaticamente por ej: "Top 50"
    MOOVIE_LIST_TYPE_DEFAULT_PRIVATE(4); //Listas creadas automaticamente por ej: "Watchlist"

    private final int type;
    MoovieListTypes(int size) {
        this.type = size;
    }

    public int getType() {
        return type;
    }
}

