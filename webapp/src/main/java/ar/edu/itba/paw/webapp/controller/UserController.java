package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;

    private final UserService userService;

    @Context
    private UriInfo uriInfo;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(final UserService userService, VerificationTokenService verificationTokenService){
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        if (page<DEFAULT_PAGE_INT)
            return Response.status(Response.Status.BAD_REQUEST).build();

        final List<User> all = userService.listAll(page);

        if(all.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
        return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {}).build();
    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") final int id){
        final User user = userService.findUserById(id);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/email/{email}")
    public Response findUserByEmail(@PathParam("email") final String email){
        final User user = userService.findUserByEmail(email);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/username/{username}")
    public Response findUserByUsername(@PathParam("username") final String username){
        final User user = userService.findUserByUsername(username);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/usersCount")
    public Response getUserCount(){
        return Response.ok(userService.getUserCount()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserCreateDto userCreateDto) {
        final User user = userService.createUserFromUnregistered(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
        return Response.created(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getUserId())).build()).entity(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/profile/{username}")
    public Response getProfileByUsername(@PathParam("username") final String username){
        final Profile profile = userService.getProfileByUsername(username);
        if(profile == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfileDto.fromProfile(profile, uriInfo)).build();
    }

    @GET
    @Path("/{username}/image")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("username") final String username){
        final byte[] image = userService.getProfilePicture(username);
        if(image == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(image).build();
    }

    @PUT
    @Path("/{username}/image")
    @Consumes("image/png")
    public Response setProfileImage(@PathParam("username") final String username, final MultipartFile image){
        userService.setProfilePicture(image);
        return Response.ok().build();
    }


}