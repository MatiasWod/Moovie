package ar.edu.itba.paw.models.MoovieList;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoovieListCard {
    private final int moovieListId;
    private final String username;
    private final String description;
    private final int likeCount;
    private List<String> images;

    public MoovieListCard(int moovieListId, String username, String description, int likeCount, Array images) {
        this.moovieListId = moovieListId;
        this.username = username;
        this.description = description;
        this.likeCount = likeCount;
        try{
            this.images = Arrays.asList((String[]) images.getArray());
        }
        catch (SQLException e){
            this.images = new ArrayList<>();
        }
    }

    public int getMoovieListId() {
        return moovieListId;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public List<String> getImages() {
        return images;
    }

}
