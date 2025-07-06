package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.BannedMessage.BannedMessage;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;
import ar.edu.itba.paw.webapp.dto.in.*;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.mappers.InvalidAuthenticationLevelRequiredToPerformActionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToBanUserEM;
import ar.edu.itba.paw.webapp.mappers.UnableToChangeRoleEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Api(value = "/users")
@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final BannedService bannedService;
    private final ModeratorService moderatorService;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService, VerificationTokenService verificationTokenService, BannedService bannedService, ModeratorService moderatorService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.bannedService=bannedService;
        this.moderatorService= moderatorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    @GET
    @Produces(VndType.APPLICATION_USER_LIST)
    public Response searchUsers(@QueryParam("search") final String search,
                                @QueryParam("role") final Integer role,
                                @QueryParam("orderBy") final String orderBy,
                                @QueryParam("sortOrder") final String sortOrder,
                                @QueryParam("pageSize") @DefaultValue("-1") final int pageSize,
                                @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        int pageSizeQuery = pageSize;
        if (pageSize < 1 || pageSize > PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize()) {
            pageSizeQuery = PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize();
        }


        // Validar número de página
        if (pageNumber < DEFAULT_PAGE_INT) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            final List<User> all;
            final int totalCount;
            if (search!= null){
                all = userService.searchUsers(search, orderBy, sortOrder, pageSizeQuery, pageNumber);
                totalCount = userService.getSearchCount(search);
            } else if (role != null) {
                UserRoles enumRole = UserRoles.getRoleFromInt(role);
                if (enumRole == null) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                all = userService.listAll(role, pageSizeQuery,pageNumber);
                totalCount = userService.getUserCount(UserRoles.getRoleFromInt(role));
            } else {
                all = userService.listAll(pageSizeQuery,pageNumber);
                totalCount = userService.getUserCount();
            }
            if (all.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }

            List<UserDto> userDtoList = UserDto.fromUserList(all, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserDto>>(userDtoList) {
            });
            final PagingUtils<User> toReturnUserList = new PagingUtils<>(all, pageNumber-1,pageSizeQuery, totalCount);
            ResponseUtils.setPaginationLinks(res, toReturnUserList, uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(VndType.APPLICATION_USER)
    @Consumes(VndType.APPLICATION_USER_FORM)
    public Response createUser(@Valid final UserCreateDto userCreateDto) {
        LOGGER.info("Method: createUser, Path: /users, UserCreateDto: {}", userCreateDto);
        try {
            LOGGER.info("Attempting to create user with username: {}, email: {}", userCreateDto.getUsername(),
                    userCreateDto.getEmail());
            userService.createUser(userCreateDto.getUsername(), userCreateDto.getEmail(), userCreateDto.getPassword());
            final User user = userService.findUserByUsername(userCreateDto.getUsername());
            return Response
                    .created(uriInfo.getBaseUriBuilder().path("users").path(user.getUsername()).build())
                    .entity(UserDto.fromUser(user, uriInfo)).build();
        } catch (UnableToCreateUserException e) {
            LOGGER.info("User already exists. Returning CONFLICT.");
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        } catch (RuntimeException e) {
            LOGGER.error("Error creating user: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Consumes(VndType.APPLICATION_RESEND_TOKEN_FORM)
    @Produces(VndType.APPLICATION_USER_TOKEN)
    public Response resendVerificationEmail(@Valid final TokenDto tokenDto) {
        LOGGER.info("Method: resendVerificationEmail, Path: /users/, Token: {}", tokenDto.getToken());

        try {
            final Optional<Token> tokenOptional = verificationTokenService.getToken(tokenDto.getToken());

            if (!tokenOptional.isPresent()) {
                LOGGER.info("Token not found. Returning NOT_FOUND.");
                return Response.status(Response.Status.NOT_FOUND).entity("Token not found").build();
            }
            Token token = tokenOptional.get();
            if (userService.findUserById(token.getUserId()).getRole() > 0) {
                throw new UserVerifiedException("User already verified.");
            }
            userService.resendVerificationEmail(token);

            LOGGER.info("Verification email resent successfully for user");
            return Response.ok().entity("Verification email resent successfully").build();

        } catch (VerificationTokenNotFoundException e) {
            LOGGER.error("Verification token not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity("Verification token not found").build();
        } catch (UserVerifiedException e) {
            LOGGER.error("User is already verified. Returning CONFLICT.");
            return Response.status(Response.Status.CONFLICT).entity("User is already verified").build();
        } catch (Exception e) {
            LOGGER.error("Error resending verification email: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to resend verification email")
                    .build();
        }
    }


    @POST
    @Path("reset-tokens")
    @Consumes(VndType.APPLICATION_PASSWORD_TOKEN_FORM)
    public Response createPasswordResetToken(@Valid UserEmailDto userEmailDto) {
        LOGGER.info("Method: createPasswordResetToken, Path: /users, Email: {}", userEmailDto.getEmail());
        try {
            final User user = userService.findUserByEmail(userEmailDto.getEmail());
            userService.forgotPassword(user);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.error("Error creating password reset token: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @PUT
    @Path("/{username}")
    @Consumes(VndType.APPLICATION_USER_TOKEN_FORM)
    @Produces(VndType.APPLICATION_USER_TOKEN)
    public Response verifyUser(@PathParam("username") final String username, @Valid @NotNull final TokenDto tokenDto, @Context HttpServletRequest request) {
        String tokenString = tokenDto.getToken();
        LOGGER.info("Method: verifyUser, Path: users, Token: {}", tokenString);
        try {
            final Optional<Token> tok = verificationTokenService.getToken(tokenString);
            if (tok.isPresent()) {
                Token token = tok.get();
                if (userService.confirmRegister(token)) {
                    User user = userService.findUserById(token.getUserId());
                    // No es un recuest de tipo Basic, por ende no manda token. Se pone manualmente
                    ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromContextPath(request);
                    String jwt = jwtTokenProvider.createAccessToken(builder, user);
                    String refreshToken = jwtTokenProvider.createRefreshToken(builder, user);
                    return Response.ok(UserDto.fromUser(user, uriInfo))
                            .header("Moovie-AuthToken", jwt)
                            .header("Moovie-RefreshToken", refreshToken)
                            .build();
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

    @PUT
    @Path("/{username}")
    @Consumes(VndType.APPLICATION_USER_PASSWORD)
    public Response resetPassword(@PathParam("username") final String username,@Valid UserResetPasswordDto userResetPasswordDto) {
        LOGGER.info("Method: resetPassword, Path: /users, Token: {}", userResetPasswordDto.getToken());
        try {
            final Optional<Token> tokenOptional = verificationTokenService.getToken(userResetPasswordDto.getToken());
            if (!tokenOptional.isPresent()) {
                LOGGER.info("Token not found. Returning NOT_FOUND.");
                return Response.status(Response.Status.NOT_FOUND).entity("Token not found").build();
            }
            Token tok = tokenOptional.get();
            userService.resetPassword(tok, userResetPasswordDto.getPassword());
            return Response.noContent().build();
        } catch (RuntimeException e) {
            LOGGER.error("Error reset passoword: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }

    }

    @GET
    @Produces(VndType.APPLICATION_USER_LIST)
    @Path("/milkyLeaderboard")
    public Response getMilkyLeaderboard(@QueryParam("page") @DefaultValue("1") final int page,
                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        LOGGER.info("Method: getMilkyLeaderboard, Path: /users/milkyLeaderboard, Page: {}, PageSize: {}", page, pageSize);
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize()) {
                pageSizeQuery = PagingSizes.MILKY_LEADERBOARD_DEFAULT_PAGE_SIZE.getSize();
            }
            List<UserDto> leaderboards = UserDto.fromUserList(userService.getMilkyPointsLeaders(pageSizeQuery, page), uriInfo);
            int totalCount = userService.getUserCount();
            
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserDto>>(leaderboards) {});
            final PagingUtils<UserDto> pagingUtils = new PagingUtils<>(leaderboards, page, pageSizeQuery, totalCount);
            ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            LOGGER.error("Error retrieving milky leaderboard: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{username}")
    @Produces(VndType.APPLICATION_USER)
    public Response getUserByUsername(@PathParam("username") final String username, @Context Request request) {
        LOGGER.info("Method: getUserByUsername, Path: /users/{username}, Username: {}", username);
        try {
            final User user = userService.findUserByUsername(username);
            final Supplier<UserDto> dtoSupplier = () ->UserDto.fromUser(user, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, user.hashCode());
        } catch (UnableToFindUserException e) {
            LOGGER.error("Error retrieving user: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        catch (RuntimeException e) {
            LOGGER.error("Error retrieving user: {}", e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

// MODERATION

    @GET
    @Path("/{username}/banMessage")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_USER_BAN_MESSAGE)
    public Response banMessage(@PathParam("username") final String username) {
        try {
            User user = userService.findUserByUsername(username);
            BannedMessage message = bannedService.getBannedMessage(user.getUserId());
            if (message == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            LOGGER.info("RETURNING BAN MESSAGE: " + message.getMessage());
            return Response.ok(BanMessageDTO.fromBannedMessage(message, user.getUsername(), uriInfo)).build();
        } catch (BannedMessageNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PUT
    @Path("/{username}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Consumes(VndType.APPLICATION_USER_BAN_FORM)
    @Produces(VndType.APPLICATION_USER)
    public Response banUser(@PathParam("username") final String username,
                            @Valid @NotNull final BanUserDTO banUserDTO) {
        try {
            if (banUserDTO.getModAction().equals("UNBAN")) {
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
            } else if (banUserDTO.getModAction().equals("BAN")) {
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
            }
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage(), e);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @PUT
    @Path("/{username}")
    @PreAuthorize("@accessValidator.isUserAdmin()")
    @Produces(VndType.APPLICATION_USER)
    public Response makeUserMod(@PathParam("username") final String username) {
        User user = null;
        try {
            user = userService.findUserByUsername(username);
            moderatorService.makeUserModerator(user.getUserId());
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        // Actualizar el modelo a devolver
        // Llegar aqui implica un exito en la operacion
        user.setRole(UserRoles.MODERATOR.getRole());
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }
}
