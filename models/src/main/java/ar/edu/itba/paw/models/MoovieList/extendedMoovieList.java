package ar.edu.itba.paw.models.MoovieList;

public class extendedMoovieList extends MoovieList {
    private final String owner;
    private final int likes;
    private final String[] posters;

    private final int mooviesCount;

    private final int tvseriesCount;

    public extendedMoovieList(MoovieList list, String owner, int likes,int mooviesCount,int  tvseriesCount,String[] posters) {
        super(list.getMoovieListId(), list.getUserId(), list.getName(),list.getDescription(),list.getType());
        this.owner = owner;
        this.likes = likes;
        this.posters = posters;
        this.mooviesCount = mooviesCount;
        this.tvseriesCount = tvseriesCount;

    }
    public String getOwner() {
        return owner;
    }

    public int getLikes() {
        return likes;
    }

    public String[] getPosters() {
        return posters;
    }

    public int getMooviesCount() {
        return mooviesCount;
    }

    public int getTvseriesCount() {
        return tvseriesCount;
    }
}