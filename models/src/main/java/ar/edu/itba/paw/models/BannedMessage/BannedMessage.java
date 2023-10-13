package ar.edu.itba.paw.models.BannedMessage;

public class BannedMessage {
    private final int modUserId;
    private final String modUsername;
    private final String message;

    public BannedMessage(int modUserId, String modUsername, String message) {
        this.modUserId = modUserId;
        this.modUsername = modUsername;
        this.message = message;
    }

    public int getModUserId() {
        return modUserId;
    }

    public String getModUsername() {
        return modUsername;
    }

    public String getMessage() {
        return message;
    }
}
