package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.BannedMessage.BannedMessage;

import java.util.Optional;

public interface BannedDao {
    Optional<BannedMessage> getBannedMessage(int userId);

    void createBannedMessage(int bannedUserId, int modUserId , String message);

    void deleteBannedMessage(int bannedUserId);
}
