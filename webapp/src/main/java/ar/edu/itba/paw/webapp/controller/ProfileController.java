package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.out.ProfileDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/profiles")
@Component
public class ProfileController {

    private static int DEFAULT_PAGE_INT = 1;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProfiles(@QueryParam("username") final String username,
                                   @QueryParam("orderBy") final String orderBy,
                                   @QueryParam("sortOrder") final String sortOrder,
                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            List<Profile> profileList = userService.searchUsers(username, orderBy, sortOrder, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber);
            final int profileCount = userService.getSearchCount(username);
            List<ProfileDto> profileDtoList = ProfileDto.fromProfileList(profileList, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ProfileDto>>(profileDtoList) {
            });
            final PagingUtils<Profile> toReturnProfileList = new PagingUtils<>(profileList, pageNumber, PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), profileCount);
            ResponseUtils.setPaginationLinks(res, toReturnProfileList, uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/milkyLeaderboard")
    public Response getMilkyLeaderboard(@QueryParam("page") @DefaultValue("1") final int page,
                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        LOGGER.info("Method: getMilkyLeaderboard, Path: /users/milkyLeaderboard, Page: {}, PageSize: {}", page, pageSize);
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize();
            }
            List<ProfileDto> leaderboards = ProfileDto.fromProfileList(userService.getMilkyPointsLeaders(pageSizeQuery, page), uriInfo);
            return Response.ok(new GenericEntity<List<ProfileDto>>(leaderboards) {
            }).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving milky leaderboard: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}")
    public Response getProfileByUsername(@PathParam("username") final String username) {
        LOGGER.info("Method: getProfileByUsername, Path: /users/profile/{username}, Username: {}", username);
        try {
            final Profile profile = userService.getProfileByUsername(username);
            if (profile == null) {
                LOGGER.info("Profile with username {} not found. Returning NOT_FOUND.", username);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(ProfileDto.fromProfile(profile, uriInfo)).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving profile: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}/image")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("username") final String username) {
        try {
            LOGGER.info("Method: getProfileImage, Path: /users/{username}/image, Username: {}", username);
            final byte[] image = userService.getProfilePicture(username);
            return Response.ok(image).build();
        }catch (RuntimeException e) {
            LOGGER.info("Image with username {} not found. Returning NOT_FOUND.", username);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{username}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateProfileImage(@PathParam("username") String username,
                                       @FormDataParam("image") final FormDataBodyPart image,
                                       @Size(max = 1024 * 1024 * 2) @FormDataParam("image") byte[] imageBytes) {
        LOGGER.info("Method: setProfileImage, Path: /users/{username}/image, Username: {}", username);

        userService.setProfilePicture(imageBytes, image.getMediaType().getSubtype());

        LOGGER.info("Profile image updated for username: {}", username);
        return Response.ok().build();
    }

}
