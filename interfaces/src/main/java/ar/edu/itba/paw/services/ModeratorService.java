package ar.edu.itba.paw.services;

public interface ModeratorService {
    //Deletes a Media Review
    void deleteReview(int reviewId);
    //Delete a Moovie List
    void deleteMoovieListList(int moovieListId);

    //Bans an user
    void banUser(int userId);
    //Chane the tole of an user to Moderator
    void makeUserModerator(int userId);
}
