package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;

import javax.ws.rs.core.UriInfo;
import java.util.List;


public class ActorDto {
    private int actorId;
    private String actorName;
    private String profilePath;
    private String url;

    public static ActorDto fromActor(Actor actor, UriInfo uriInfo) {
        ActorDto dto = new ActorDto();
        dto.actorId = actor.getActorId();
        dto.actorName = actor.getActorName();
        dto.profilePath = actor.getProfilePath();
        dto.url = uriInfo.getBaseUriBuilder().path("actor/{actorId}").build(actor.getActorId()).toString();
        return dto;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
