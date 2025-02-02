package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;

import ar.edu.itba.paw.webapp.dto.in.BanUserDTO;
import ar.edu.itba.paw.webapp.dto.in.JustIdDto;
import ar.edu.itba.paw.webapp.dto.in.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.out.UserDto;
import ar.edu.itba.paw.webapp.dto.out.UserListIdDto;

import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.mappers.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;

    private final UserService userService;
    private final MoovieListService moovieListService;
    private final VerificationTokenService verificationTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModeratorService moderatorService;
    private final MediaService mediaService;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService, MoovieListService moovieListService,
                          VerificationTokenService verificationTokenService, JwtTokenProvider jwtTokenProvider, ModeratorService moderatorService,
                          MediaService mediaService) {
        this.userService = userService;
        this.moovieListService = moovieListService;
        this.verificationTokenService = verificationTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.moderatorService = moderatorService;
        this.mediaService = mediaService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("email") final String email,
            @QueryParam("id") final Integer id) {

        // Si se proporciona un ID, buscar por ID
        if (id != null) {
            try {
                final User user = userService.findUserById(id);
                if (user == null) {
                    LOGGER.info("User with ID {} not found. Returning NOT_FOUND.", id);
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(UserDto.fromUser(user, uriInfo)).build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }

        // Buscar por email si se proporciona
        if (email != null && !email.isEmpty()) {
            try {
                final User user = userService.findUserByEmail(email);
                if (user == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(UserDto.fromUser(user, uriInfo)).build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        }

        // Validar número de página
        if (page < DEFAULT_PAGE_INT) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Listar usuarios paginados
        try {
            final List<User> all = userService.listAll(page);
            if (all.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
            return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {}).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid final UserCreateDto userCreateDto) {
        LOGGER.info("Method: createUser, Path: /users, UserCreateDto: {}", userCreateDto);
        try {
            LOGGER.info("Attempting to create user with username: {}, email: {}", userCreateDto.getUsername(), userCreateDto.getEmail());
            userService.createUser(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
            final User user = userService.findUserByUsername(userCreateDto.getUsername());
            return Response.created(uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getUserId())).build()).entity(UserDto.fromUser(user, uriInfo)).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error creating user: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/count")
    public Response getUserCount() {
        LOGGER.info("Method: getUserCount, Path: /users/count");
        try {
            int count = userService.getUserCount();
            LOGGER.info("User count retrieved: {}", count);
            return Response.ok().entity(new GenericEntity<Integer>(count) {}).build();
        } catch (Exception e) {
            LOGGER.error("Error retrieving user count: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}")
    public Response getUserByUsername(@PathParam("username") final String username) {
        LOGGER.info("Method: getUserByUsername, Path: /users/{username}, Username: {}", username);
        try {
            final User user = userService.findUserByUsername(username);
            if (user == null) {
                LOGGER.info("User with username {} not found. Returning NOT_FOUND.", username);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(UserDto.fromUser(user, uriInfo)).build();
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving user: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/verify/{token}")
    public Response verifyUser(@PathParam("token") String tokenString) {
        LOGGER.info("Method: verifyUser, Path: /users/verify/{token}, Token: {}", tokenString);
        try {
            final Optional<Token> tok = verificationTokenService.getToken(tokenString);
            if (tok.isPresent()) {
                Token token = tok.get();
                if (userService.confirmRegister(token)) {
                    User user = userService.findUserById(token.getUserId());
                    return Response.notModified().header(HttpHeaders.AUTHORIZATION, jwtTokenProvider.createToken(user)).build();
                }
                LOGGER.info("Token validation failed. Returning INTERNAL_SERVER_ERROR.");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            LOGGER.info("Token not found. Returning BAD_REQUEST.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (VerificationTokenNotFoundException e) {
            LOGGER.error("Verification token not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    MODERATION

    @PUT
    @Path("/{username}/ban")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response banUser(@PathParam("username") final String username,
                            @Valid final BanUserDTO banUserDTO) {
        try {
            User toBan = userService.findUserByUsername(username);
            try {
                this.moderatorService.banUser(toBan.getUserId(), banUserDTO.getBanMessage());
            } catch (UnableToBanUserException e) {
                return new UnableToBanUserEM().toResponse(e);
            } catch (InvalidAuthenticationLevelRequiredToPerformActionException e) {
                return new InvalidAuthenticationLevelRequiredToPerformActionEM().toResponse(e);
            }
            User finalUser = userService.findUserByUsername(username);
            return Response.ok(UserDto.fromUser(finalUser, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
    }

    @PUT
    @Path("/{username}/unban")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unbanUser(@PathParam("username") final String username) {

        try {
            User toUnban = userService.findUserByUsername(username);
            try {
                moderatorService.unbanUser(toUnban.getUserId());
            } catch (InvalidAuthenticationLevelRequiredToPerformActionException e) {
                return new InvalidAuthenticationLevelRequiredToPerformActionEM().toResponse(e);
            } catch (UnableToChangeRoleException e) {
                return new UnableToChangeRoleEM().toResponse(e);
            }
            User finalUser = userService.findUserByUsername(username);
            return Response.ok(UserDto.fromUser(finalUser, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }
    }


}
