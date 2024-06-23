package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;
import ar.edu.itba.paw.webapp.dto.ProfileDto;
import ar.edu.itba.paw.webapp.dto.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;

    private final UserService userService;

    private final VerificationTokenService verificationTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(final UserService userService, VerificationTokenService verificationTokenService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        if (page < DEFAULT_PAGE_INT)
            return Response.status(Response.Status.BAD_REQUEST).build();

        final List<User> all = userService.listAll(page);

        if (all.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
        return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {
        }).build();
    }


    // users/authtest
    @GET
    @Path("/authtest")
    public Response authTest() {
        return Response.ok("Hello authenticated user").build();
    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") final int id) {
        final User user = userService.findUserById(id);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/email/{email}")
    public Response findUserByEmail(@PathParam("email") final String email) {
        final User user = userService.findUserByEmail(email);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/username/{username}")
    public Response findUserByUsername(@PathParam("username") final String username) {
        final User user = userService.findUserByUsername(username);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/usersCount")
    public Response getUserCount() {
        return Response.ok(userService.getUserCount()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserCreateDto userCreateDto) {
        try {
            LOGGER.info("attempting to create user {} {} {}", userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());

            userService.createUser(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
            final User user = userService.findUserByUsername(userCreateDto.getUsername());

//            Este metodo no funciona! el otro metodo maneja el envio del mail de forma automatica, me parece mucha mejor opcion
//            final User user = userService.createUserFromUnregistered(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());

            return Response.created(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getUserId())).build()).entity(UserDto.fromUser(user, uriInfo)).build();
        } catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @PUT
    @Path("/verify/{token}")
    public Response verifyUser(@PathParam("token") String tokenString) {
        try {
            final Optional<Token> tok = verificationTokenService.getToken(tokenString);
            if (tok.isPresent()) {
                Token token = tok.get();
                if (userService.confirmRegister(token)) {
                    User user = userService.findUserById(token.getUserId());
                    return Response.notModified().header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createToken(user)).build();
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
//            TODO: should refactor to resend token!
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (VerificationTokenNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }


    @GET
    @Path("/profile/{username}")
    public Response getProfileByUsername(@PathParam("username") final String username) {
        final Profile profile = userService.getProfileByUsername(username);
        if (profile == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(ProfileDto.fromProfile(profile, uriInfo)).build();
    }

    @GET
    @Path("/{username}/image")
    @Produces("image/png")
    public Response getProfileImage(@PathParam("username") final String username) {
        final byte[] image = userService.getProfilePicture(username);
        if (image == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(image).build();
    }

    @PUT
    @Path("/{username}/image")
    @Consumes("image/png")
    public Response setProfileImage(@PathParam("username") final String username, final MultipartFile image) {
        userService.setProfilePicture(image);
        return Response.ok().build();
    }


}