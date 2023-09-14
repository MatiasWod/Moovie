package ar.edu.itba.paw.models.MediaList;

public class MediaList {
    private final int mediaListId;
    private final int userId;
    private final String name;
    private final String description;

    public MediaList(int mediaListId, int userId, String name, String description) {
        this.mediaListId = mediaListId;
        this.userId = userId;
        this.name = name;
        this.description = description;
    }

    public int getMediaListId() {
        return mediaListId;
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
