package ar.edu.itba.paw.models.MoovieList;

import java.util.List;

public class MoovieListDetails {
    private final MoovieListCard card;
    private final List<MoovieListContent> content;

    public MoovieListDetails(MoovieListCard card, List<MoovieListContent> content) {
        this.card = card;
        this.content = content;
    }

    public MoovieListCard getCard() {
        return card;
    }

    public List<MoovieListContent> getContent() {
        return content;
    }
}
