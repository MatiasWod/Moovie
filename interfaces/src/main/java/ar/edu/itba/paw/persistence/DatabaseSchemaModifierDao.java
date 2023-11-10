package ar.edu.itba.paw.persistence;

public interface DatabaseSchemaModifierDao {
    void updateGenres();
    void updateProviders();
    void updateActors();
    void updateCreators();
}
