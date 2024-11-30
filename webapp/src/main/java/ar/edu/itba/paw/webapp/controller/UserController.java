package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.services.VerificationTokenService;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;
import ar.edu.itba.paw.webapp.dto.in.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewDto;
import ar.edu.itba.paw.webapp.dto.out.ProfileDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.dto.out.UserDto;
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
    private final ReviewService reviewService;
    private final VerificationTokenService verificationTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService, ReviewService reviewService, VerificationTokenService verificationTokenService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.verificationTokenService = verificationTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        LOGGER.info("Method: listAll, Path: /users, Page: {}", page);
        if (page < DEFAULT_PAGE_INT) {
            LOGGER.warn("Invalid page number: {}. Returning BAD_REQUEST.", page);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final List<User> all = userService.listAll(page);
        if (all.isEmpty()) {
            LOGGER.info("No users found. Returning NOT_FOUND.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
        return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {
        }).build();
    }

    @GET
    @Path("/authtest")
    public Response authTest() {
        LOGGER.info("Method: authTest, Path: /users/authtest");
        return Response.ok("Hello authenticated user").build();
    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") final int id) {
        LOGGER.info("Method: findUserById, Path: /users/{id}, ID: {}", id);
        final User user = userService.findUserById(id);
        if (user == null) {
            LOGGER.info("User with ID {} not found. Returning NOT_FOUND.", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/email/{email}")
    public Response findUserByEmail(@PathParam("email") final String email) {
        LOGGER.info("Method: findUserByEmail, Path: /users/email/{email}, Email: {}", email);
        final User user = userService.findUserByEmail(email);
        if (user == null) {
            LOGGER.info("User with email {} not found. Returning NOT_FOUND.", email);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/username/{username}")
    public Response findUserByUsername(@PathParam("username") final String username) {
        LOGGER.info("Method: findUserByUsername, Path: /users/username/{username}, Username: {}", username);
        final User user = userService.findUserByUsername(username);
        if (user == null) {
            LOGGER.info("User with username {} not found. Returning NOT_FOUND.", username);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    @GET
    @Path("/usersCount")
    public Response getUserCount() {
        LOGGER.info("Method: getUserCount, Path: /users/usersCount");
        try {
            int count = userService.getUserCount();
            LOGGER.info("User count retrieved: {}", count);
            return Response.ok(count).build();
        } catch (Exception e) {
            LOGGER.error("Error retrieving user count: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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

    @GET
    @Path("/profile/{username}")
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
        LOGGER.info("Method: getProfileImage, Path: /users/{username}/image, Username: {}", username);
        final byte[] image = userService.getProfilePicture(username);
        return Response.ok(image).build();
    }

    @PUT
    @Path("/{username}/image")
    @Consumes("image/png")
    public Response setProfileImage(@PathParam("username") final String username, final MultipartFile image) {
        LOGGER.info("Method: setProfileImage, Path: /users/{username}/image, Username: {}", username);
        userService.setProfilePicture(image);
        LOGGER.info("Profile image updated for username: {}", username);
        return Response.ok().build();
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

    /* REVIEWS */
    @GET
    @Path("/{id}/reviews")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieReviewsFromUser(@PathParam("id") final int userId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            final List<Review> reviews = reviewService.getMovieReviewsFromUser(userId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            return Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {}).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /* MOOVIELISTREVIEWS */
    @GET
    @Path("/{id}/moovieListReviews")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListReviewsFromUser(@PathParam("id") final int userId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {

        try {
            final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsFromUser(userId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
            return Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {}).build();
        } catch (UnableToFindUserException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
