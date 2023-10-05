package ar.edu.itba.paw.models.MoovieList;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoovieListCard {
    private final int moovieListId;
    private final String name;
    private final String username;
    private final String description;
    private final int likeCount;
    private final int type;
    private final int size;
    private List<String> images;

    public MoovieListCard(int moovieListId, String name,  String username, String description, int likeCount, int type, int size, Array images) {
        this.moovieListId = moovieListId;
        this.name = name;
        this.username = username;
        this.description = description;
        this.likeCount = likeCount;
        this.type = type;
        this.size = size;
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

    public String getName(){
        return name;
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

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public List<String> getImages() {
        return images;
    }

}
