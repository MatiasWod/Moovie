package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.NoFileException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
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

@Path("images")
@Component
public class ImageController {
    @Context
    private UriInfo uriInfo;
    private final UserService userService;
    private final ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    public ImageController(final UserService userService, final ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @GET
    @Path("/{id}")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("id") final int id) {
        try {
            LOGGER.info("Method: getProfileImage, Path: /images/{id}, Id: {}", id);
            if (!imageService.getImageById(id).isPresent()) {
                throw new NoFileException("No image found for the given ID.");
            }
            final byte[] image = imageService.getImageById(id).get().getImage();
            Response.ResponseBuilder res = Response.ok(image);
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
                                       @Size(max = 1024 * 1024 * 2) @FormDataParam("image") byte[] imageBytes) {
        try {
            final int userId = userService.tryToGetCurrentUserId();
            imageService.setUserImage(userId, imageBytes, image.getMediaType().getSubtype());
            LOGGER.info("Method: setProfileImage, Path: /images/{id}, UserId: {}", userId);
            return Response.ok().build();
        } catch (UnableToFindUserException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error updating profile image: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
