package ar.edu.itba.paw.webapp.dto.in;

import javax.validation.constraints.NotNull;

public class BanUserDTO {

    @NotNull
    private String banMessage;

    public String getBanMessage() {
        return banMessage;
    }

    public void setBanMessage(String banMessage) {
        this.banMessage = banMessage;
    }
}
