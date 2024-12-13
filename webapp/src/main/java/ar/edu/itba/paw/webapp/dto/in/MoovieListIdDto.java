package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;

public class MoovieListIdDto {
    @NotNull
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
