package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.User.Profile;
import ar.edu.itba.paw.models.User.Token;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.auth.JwtTokenProvider;
import ar.edu.itba.paw.webapp.dto.in.BanUserDTO;
import ar.edu.itba.paw.webapp.dto.in.JustIdDto;
import ar.edu.itba.paw.webapp.dto.in.UserCreateDto;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.exceptions.VerificationTokenNotFoundException;
import ar.edu.itba.paw.webapp.mappers.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
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
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        LOGGER.info("Method: listAll, Path: /users, Page: {}", page);
        if (page < DEFAULT_PAGE_INT) {
            LOGGER.warn("Invalid page number: {}. Returning BAD_REQUEST.", page);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            final List<User> all = userService.listAll(page);
            if (all.isEmpty()) {
                LOGGER.info("No users found. Returning NOT_FOUND.");
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
            return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {
            }).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/authtest")
    public Response authTest() {
        return Response.ok("Hello authenticated user").build();
    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") final int id) {
        LOGGER.info("Method: findUserById, Path: /users/{id}, ID: {}", id);
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

    @GET
    @Path("/email/{email}")
    public Response findUserByEmail(@PathParam("email") final String email) {
        try {
            LOGGER.info("Method: findUserByEmail, Path: /users/email/{email}, Email: {}", email);
            final User user = userService.findUserByEmail(email);
            if (user == null) {
                LOGGER.info("User with email {} not found. Returning NOT_FOUND.", email);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(UserDto.fromUser(user, uriInfo)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

    @GET
    @Path("/username/{username}")
    public Response findUserByUsername(@PathParam("username") final String username) {
        try {
            LOGGER.info("Method: findUserByUsername, Path: /users/username/{username}, Username: {}", username);
            final User user = userService.findUserByUsername(username);
            if (user == null) {
                LOGGER.info("User with username {} not found. Returning NOT_FOUND.", username);
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(UserDto.fromUser(user, uriInfo)).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@QueryParam("username") final String username,
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


    /***
     * LIKES
     */

    // Returns a list of likes
    @GET
    @Path("/{username}/listLikes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLikedLists(@PathParam("username") final String username,
                                  @QueryParam("orderBy") String orderBy,
                                  @QueryParam("order") String order,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        List<MoovieListCard> mlcList = moovieListService.getLikedMoovieListCards(username, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);



        int listCount = userService.getLikedMoovieListCountForUser(username);

        List<UserListIdDto> listToRet = new ArrayList<UserListIdDto>();
        for ( MoovieListCard mlc : mlcList){
            listToRet.add(new UserListIdDto(mlc.getMoovieListId(), username));
        }

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserListIdDto>>(listToRet) {
        });
        final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(mlcList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), listCount);
        ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
        return res.build();
    }

    // Returns like status for a specific media
    @GET
    @Path("/{username}/listLikes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserLikedListById(@PathParam("id") final int id,
                                         @PathParam("username") final String username) {
        UserMoovieListId userMoovieListId = moovieListService.currentUserHasLiked(id);
        if (userMoovieListId != null && userMoovieListId.getUsername().equals(username)) {
            return Response.ok(new UserListIdDto().fromUserMoovieList(userMoovieListId, username)).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{username}/listLikes")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response likeMoovieList(@PathParam("username") String username,
                                                    @Valid JustIdDto idDto) {
        userService.isUsernameMe(username);
        boolean like = moovieListService.likeMoovieList(idDto.getId());
        if (like) {
            return Response.ok()
                    .entity("{\"message\":\"Succesfully liked list.\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"Lista is already liked.\"}").build();
    }


    @DELETE
    @Path("/{username}/listLikes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response unlikeMoovieList(@PathParam("username") String username,
                                                      @PathParam("id") int id) {
        userService.isUsernameMe(username);
        moovieListService.removeLikeMoovieList(id);
        return Response.ok()
                .entity("{\"message\":\"Succesfully unliked list.\"}").build();
    }

    /***
     * FOLLOWS
     */

    @GET
    @Path("/{username}/listFollows")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowedLists(@PathParam("username") final String username,
                                  @QueryParam("orderBy") String orderBy,
                                  @QueryParam("order") String order,
                                  @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        int userid = userService.getProfileByUsername(username).getUserId();
        List<MoovieListCard> mlcList = moovieListService.getFollowedMoovieListCards(userid, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);

        int listCount = moovieListService.getFollowedMoovieListCardsCount(userid, MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());

        List<UserListIdDto> listToRet = new ArrayList<UserListIdDto>();
        for ( MoovieListCard mlc : mlcList){
            listToRet.add(new UserListIdDto(mlc.getMoovieListId(), username));
        }

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserListIdDto>>(listToRet) {
        });
        final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(mlcList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), listCount);
        ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
        return res.build();
    }

    @GET
    @Path("/{username}/listFollows/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserFollowedListById(@PathParam("username") String username,
                                            @PathParam("id") final int id) {
        UserMoovieListId userMoovieListId = moovieListService.currentUserHasFollowed(id);
        if (userMoovieListId != null && userMoovieListId.getUsername().equals(username)) {
            return Response.ok(new UserListIdDto().fromUserMoovieList(userMoovieListId, username)).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{username}/listFollows")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response followMoovieList(@PathParam("username") String username,
                                                      @Valid JustIdDto idDto) {
        userService.isUsernameMe(username);
        boolean like = moovieListService.followMoovieList(idDto.getId());
        if (like) {
            return Response.ok()
                    .entity("{\"message\":\"Succesfully followed list.\"}").build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"List is already followed.\"}").build();
    }

    @DELETE
    @Path("/{username}/listFollows/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response unfollowMoovieList(@PathParam("username") String username,
                                                        @PathParam("id") int id) {
        userService.isUsernameMe(username);
        moovieListService.removeFollowMoovieList(id);
        return Response.ok()
                .entity("{\"message\":\"Succesfully unfollowed list.\"}").build();
    }


    /***
     * Watched
     */

    @GET
    @Path("/{username}/watched")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWatched(@PathParam("username") final String username,
                                     @QueryParam("orderBy") String orderBy,
                                     @QueryParam("order") String order,
                                     @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        MoovieListCard ml = moovieListService.getMoovieListCards("Watched", username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),
                null, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 1).get(0);

        List<Media> mediaList = moovieListService.getMoovieListContent(ml.getMoovieListId(),orderBy,order,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber);

        int mediaCount = ml.getSize();
        List<MediaIdDto> listToRet = new ArrayList<>();

        for ( Media media : mediaList){
            listToRet.add(new MediaIdDto(media.getMediaId(), username));
        }

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdDto>>(listToRet) {
        });

        final PagingUtils<Media> toReturnMoovieListCardList = new PagingUtils<>(mediaList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), mediaCount);
        ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
        return res.build();
    }


    @GET
    @Path("/{username}/watched/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWatchedMediaByMediaId(@PathParam("username") final String username,
                                              @PathParam("id") final int mediaId) {
        userService.isUsernameMe(username);
        boolean watched = mediaService.getMediaById(mediaId).isWatched();
        if(watched){
            return Response.ok(new MediaIdDto(mediaId, username)).build();
        }
        return Response.noContent().build();
    }


    @POST
    @Path("/{username}/watched")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertIntoWatched(@PathParam("username") final String username,
                                      @Valid  final JustIdDto justIdDto){
        moovieListService.addMediaToWatched(justIdDto.getId(), username);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{username}/watched/{id}")
    public Response deleteFromWatched(@PathParam("username") final String username,
                                      @PathParam("id") final int id){
        moovieListService.removeMediaFromWatched(id, username);
        return Response.ok().build();
    }



    /***
     * Watchlist
     */

    @GET
    @Path("/{username}/watchlist")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWatchlist(@PathParam("username") final String username,
                               @QueryParam("orderBy") String orderBy,
                               @QueryParam("order") String order,
                               @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {

        MoovieListCard ml = moovieListService.getMoovieListCards("Watchlist", username,
                MoovieListTypes.MOOVIE_LIST_TYPE_DEFAULT_PRIVATE.getType(),
                null, null, PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(), 1).get(0);

        List<Media> mediaList = moovieListService.getMoovieListContent(ml.getMoovieListId(),orderBy,order,PagingSizes.MEDIA_DEFAULT_PAGE_SIZE.getSize(),pageNumber);

        int mediaCount = ml.getSize();
        List<MediaIdDto> listToRet = new ArrayList<>();

        for ( Media media : mediaList){
            listToRet.add(new MediaIdDto(media.getMediaId(), username));
        }

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdDto>>(listToRet) {
        });

        final PagingUtils<Media> toReturnMoovieListCardList = new PagingUtils<>(mediaList, pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), mediaCount);
        ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
        return res.build();
    }

    @GET
    @Path("/{username}/watchlist/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWatchlistMediaByMediaId(@PathParam("username") final String username,
                                              @PathParam("id") final int mediaId) {
        userService.isUsernameMe(username);
        boolean watchlist = mediaService.getMediaById(mediaId).isWatchlist();
        if(watchlist){
            return Response.ok(new MediaIdDto(mediaId, username)).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Path("/{username}/watchlist")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertIntoWatchlist(@PathParam("username") final String username,
                                      @Valid  final JustIdDto justIdDto){
        moovieListService.addMediaToWatchlist(justIdDto.getId(), username);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{username}/watchlist/{id}")
    public Response deleteFromWatchlist(@PathParam("username") final String username,
                                      @PathParam("id") final int id){
        moovieListService.removeMediaFromWatchlist(id, username);
        return Response.ok().build();
    }


}
