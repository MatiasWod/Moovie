package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;

public class CreateListForm {
    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,}")
    @NotEmpty(message = "Please enter an email")
    private String userEmail;
    private List<Integer> mediaIdsList;
    @NotEmpty(message = "Please enter a list name")
    private String listName;
    private String listDescription;

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListDescription(String listDescription) {
        this.listDescription = listDescription;
    }

    public void setMediaIdsList(List<Integer> mediaIdsList) {
        this.mediaIdsList = mediaIdsList;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getListName() {
        return listName;
    }

    public String getListDescription() {
        return listDescription;
    }

    public List<Integer> getMediaIdsList() {
        return mediaIdsList;
    }
}
