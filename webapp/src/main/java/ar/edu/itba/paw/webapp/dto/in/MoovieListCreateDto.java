package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MoovieListCreateDto {

    @NotNull
    @Size(min = 1, max = 254)
    @Pattern(regexp = "[^/><]+")
    private String name;

    @NotNull
    @Size(max = 1000)
    @Pattern(regexp = "[^></]*")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
