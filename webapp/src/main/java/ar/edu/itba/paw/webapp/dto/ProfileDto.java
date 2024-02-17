package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User.Profile;

import javax.ws.rs.core.UriInfo;

public class ProfileDto {
    private String username;
    private String email;
    private int role;
    private int moovieListCount;
    private int reviewsCount;
    private int milkyPoints;
    private boolean hasBadge;

    public static ProfileDto fromProfile(final Profile profile, final UriInfo uriInfo){
        final ProfileDto dto = new ProfileDto();

        dto.username = profile.getUsername();
        dto.email = profile.getEmail();
        dto.role = profile.getRole();
        dto.moovieListCount = profile.getMoovieListCount();
        dto.reviewsCount = profile.getReviewsCount();
        dto.milkyPoints = profile.getMilkyPoints();
        dto.hasBadge = profile.isHasBadge();

        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
