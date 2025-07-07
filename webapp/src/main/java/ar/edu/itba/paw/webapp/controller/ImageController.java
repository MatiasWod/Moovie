package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NoFileException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.User.Image;
import ar.edu.itba.paw.services.ImageService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("images")
@Component
public class ImageController {
    @Context
    private UriInfo uriInfo;
    private final UserService userService;
    private final ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private final static int DEFAULT_IMAGE_SIZE = 102;
    private final static String DEFAULT_IMAGE_SIZE_STRING = "102";
    private static final int MAX_IMAGE_SIZE = 1024 * 1024 * 2;

    @Autowired
    public ImageController(final UserService userService, final ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GET
    @Path("/{id}")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("id") final int id,
                                    @QueryParam("size")@DefaultValue(DEFAULT_IMAGE_SIZE_STRING) int size) {
        try {
            LOGGER.info("Method: getProfileImage, Path: /images/{id}, Id: {}", id);
            Image image = imageService.getImageById(id, size);
            final byte[] imageBytes = image.getImage();
            Response.ResponseBuilder res = Response.ok(imageBytes);
            ResponseUtils.setMaxAgeCache(res);
            return res.build();
        }catch (NoFileException | UnableToFindUserException e) {
            LOGGER.error("Error retrieving profile image: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            LOGGER.error("Error retrieving profile image: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfileImage(@FormDataParam("image") final FormDataBodyPart image,
                                       @Size(max = MAX_IMAGE_SIZE) @FormDataParam("image") byte[] imageBytes) {
        try {
            final int userId = userService.tryToGetCurrentUserId();
            int imageId = imageService.setUserImage(userId, imageBytes, image.getMediaType().getSubtype());
            LOGGER.info("Method: setProfileImage, Path: /images/{id}");
            final URI uri = uriInfo.getBaseUriBuilder()
                    .path("images")
                    .path(String.valueOf(imageId))
                    .build();
            return Response.created(uri).build();
        } catch (UnableToFindUserException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
