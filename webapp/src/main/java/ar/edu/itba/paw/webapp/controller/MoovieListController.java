package ar.edu.itba.paw.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Media.OrderedMedia;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.EditListDTO;
import ar.edu.itba.paw.webapp.dto.in.MediaListDto;
import ar.edu.itba.paw.webapp.dto.in.MoovieListCreateDto;
import ar.edu.itba.paw.webapp.dto.out.MediaIdListIdDto;
import ar.edu.itba.paw.webapp.dto.out.MoovieListDto;
import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import ar.edu.itba.paw.webapp.dto.out.UserListIdDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;

@Path("lists")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;
    private final UserService userService;
    private final ReportService reportService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService, UserService userService,
            ReportService reportService) {
        this.moovieListService = moovieListService;
        this.userService = userService;
        this.reportService = reportService;
    }

    @GET
    @Produces(VndType.APPLICATION_MOOVIELIST_LIST)
    public Response getMoovieList(
            @QueryParam("ids") final String ids,
            @QueryParam("search") String search,
            @QueryParam("ownerUsername") String ownerUsername,
            @QueryParam("type") @DefaultValue("-1") int type,
            @QueryParam("orderBy") String orderBy,
            @QueryParam("order") String order,
            @QueryParam("likedByUser") String likedBy,
            @QueryParam("followedByUser") String followedBy,
            @QueryParam("isReported") final boolean isReported,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
            if (isReported) {
                try {
                    User user = userService.getInfoOfMyUser();
                    if (user.getRole() < UserRoles.MODERATOR.getRole()) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("User is not moderator")
                                .build();
                    }
                    final List<MoovieListCard> reported = reportService.getReportedMoovieLists(
                            PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize(), pageNumber, user.getUserId());
                    int listCount = reportService.getReportedMoovieListsCount();
                    Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(
                            MoovieListDto.fromMoovieListList(reported, uriInfo)) {
                    });
                    final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(reported,
                            pageNumber, PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize(), listCount);
                    ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
                    return res.build();
                } catch (UserNotLoggedException e) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(e.getMessage())
                            .build();
                }
            }
            // Buscar por IDs si se proporcionan
            if (ids != null && !ids.isEmpty()) {
                if (ids.length() > 100) {
                    throw new IllegalArgumentException(
                            "Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }

                List<Integer> idList = new ArrayList<>();
                String[] splitIds = ids.split(",");
                for (String id : splitIds) {
                    try {
                        idList.add(Integer.parseInt(id.trim()));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                    }
                }

                if (idList.size() >= 25 || idList.size() < 0) {
                    throw new IllegalArgumentException(
                            "Invalid ids, param. A comma separated list of Media IDs. Up to 100 are allowed in a single request.");
                }

                List<MoovieListDto> mlList = new ArrayList<>();
                for (int id : idList) {
                    try {
                        MoovieListDto mlc = MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id),
                                uriInfo);
                        mlList.add(mlc);
                    } catch (InvalidAccessToResourceException e) {
                        // We just ignored the private list its trying to access
                    }
                }

                return Response.ok(new GenericEntity<List<MoovieListDto>>(mlList) {
                }).build();
            } else if (likedBy != null || followedBy != null) {
                List<MoovieListCard> mlcList = new ArrayList<>();
                int listCount = 0;
                if (followedBy != null) {
                    int userid = userService.findUserByUsername(followedBy).getUserId();
                    mlcList = moovieListService.getFollowedMoovieListCards(userid,
                            MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                            PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);
                    listCount = moovieListService.getFollowedMoovieListCardsCount(userid,
                            MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
                }
                if (likedBy != null) {
                    int userid = userService.findUserByUsername(likedBy).getUserId();
                    mlcList = moovieListService.getLikedMoovieListCards(likedBy,
                            MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                            PagingSizes.USER_LIST_DEFAULT_PAGE_SIZE.getSize(), pageNumber - 1);
                    listCount = moovieListService.getFollowedMoovieListCardsCount(userid,
                            MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType());
                }
                Response.ResponseBuilder res = Response
                        .ok(new GenericEntity<List<MoovieListDto>>(MoovieListDto.fromMoovieListList(mlcList, uriInfo)) {
                        });
                final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(mlcList, pageNumber,
                        PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), listCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);
                return res.build();
            }

            // Buscar por otros criterios si no se proporcionan IDs ni liked by ni followed
            // o liked por usuario.
            else {
                if (type < 1 || type > 4) {
                    type = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();
                }

                List<MoovieListCard> moovieListCardList = moovieListService.getMoovieListCards(search, ownerUsername,
                        type, orderBy, order, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber);
                final int moovieListCardCount = moovieListService.getMoovieListCardsCount(search, ownerUsername, type);

                List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListCardList, uriInfo);

                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto) {
                });
                final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(moovieListCardList,
                        pageNumber, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), moovieListCardCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListCardList, uriInfo);

                return res.build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieList(@Valid final MoovieListCreateDto listDto, @Context Request request) {
        try {

            MoovieListTypes moovieListType = MoovieListTypes.fromType(listDto.getType());

            int listId = moovieListService.createMoovieList(
                    listDto.getName(),
                    moovieListType.getType(),
                    listDto.getDescription()).getMoovieListId();

            final MoovieListCard list = moovieListService.getMoovieListCardById(listId);
            final Supplier<MoovieListDto> dtoSupplier = () -> MoovieListDto.fromMoovieList(list, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, list.hashCode());
        } catch (UnableToInsertIntoDatabase | DuplicateKeyException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A movie list with the same name already exists.")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_MOOVIELIST)
    public Response getMoovieListById(@PathParam("id") final int id, @Context Request request) {
        try {
            final MoovieListCard list = moovieListService.getMoovieListCardById(id);
            final Supplier<MoovieListDto> dtoSupplier = () -> MoovieListDto.fromMoovieList(list, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, list.hashCode());
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource.")).build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred.")).build();
        }

    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMoovieList(@PathParam("id") @NotNull int listId,
            @Valid @NotNull final EditListDTO editListForm) {
        try {
            moovieListService.editMoovieList(listId, editListForm.getListName(), editListForm.getListDescription());
            return Response.ok()
                    .entity("MoovieList successfully edited for MoovieList with ID: " + listId)
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("MoovieList not found.")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }

    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieList(@PathParam("id") final int id) {
        try {
            moovieListService.deleteMoovieList(id);
            return Response.ok().build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("MoovieList not found.")
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You do not have access to this resource.")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    // Likes

    @POST
    @Path("/{id}/likes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieListLike(@PathParam("id") int id) {
        boolean liked = moovieListService.likeMoovieList(id);
        if (liked)
            return Response.ok("{\"message\":\"Successfully liked list.\"}").build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"List is already liked.\"}").build();
    }

    @DELETE
    @Path("/{id}/likes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieListLike(@PathParam("id") int id) {
        boolean removed = moovieListService.removeLikeMoovieList(id);
        if (removed)
            return Response.noContent().build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"List is not liked.\"}").build();
    }

    // Returns like status for a specific list
    @GET
    @Path("/{listId}/likes/{username}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_LIST_LIKE)
    public Response getUserLikedListById(@PathParam("listId") final int listId,
            @PathParam("username") final String username) {
        if (moovieListService.userLikesMoovieList(listId, username)) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("lists")
                    .path(String.valueOf(listId))
                    .path("likes")
                    .path(username)
                    .build().toString();
            return Response.ok(new UserListIdDto(listId, username, uri, uriInfo)).build();
        }
        return Response.noContent().build();
    }

    // Return all likes for a list
    @GET
    @Path("/{listId}/likes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(VndType.APPLICATION_LIST_LIKE_LISTS) // Asumiendo un VndType para listas de usuarios
    public Response getUsersWhoLikedList(@PathParam("listId") final int listId,
            @QueryParam("page") @DefaultValue("0") final int page) {

        List<User> likedUsers = moovieListService.usersLikesMoovieList(listId, page,
                PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize());
        if (likedUsers.isEmpty()) {
            return Response.noContent().build();
        }
        List<UserListIdDto> toRet = new ArrayList<>();
        for (User user : likedUsers) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("lists")
                    .path(String.valueOf(listId))
                    .path("likes")
                    .path(user.getUsername())
                    .build().toString();
            toRet.add(new UserListIdDto(listId, user.getUsername(), uri, uriInfo));
        }
        return Response.ok(new GenericEntity<List<UserListIdDto>>(toRet) {
        }).build();
    }

    // Follows

    @POST
    @Path("/{id}/followers")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followMoovieList(@PathParam("id") int id) {
        boolean followed = moovieListService.followMoovieList(id);
        if (followed)
            return Response.ok("{\"message\":\"Successfully followed list.\"}").build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"List is already followed.\"}").build();
    }

    @DELETE
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Path("/{id}/followers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowMoovieList(@PathParam("id") int id) {
        boolean unfollowed = moovieListService.removeFollowMoovieList(id);
        if (unfollowed)
            return Response.noContent().build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\":\"List is not followed.\"}").build();
    }

    // Returns like follow for a specific list
    @GET
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Path("/{listId}/followers/{username}")
    @Produces(VndType.APPLICATION_FOLLOWED_LISTS_USER_LIST)
    public Response getUserFollowsListById(@PathParam("listId") final int listId,
            @PathParam("username") final String username) {
        if (moovieListService.userFollowsMoovieList(listId, username)) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("lists")
                    .path(String.valueOf(listId))
                    .path("followers")
                    .path(username)
                    .build().toString();
            return Response.ok(new UserListIdDto(listId, username, uri, uriInfo)).build();
        }
        return Response.noContent().build();
    }

    // Return all follows for a list
    @GET
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Path("/{listId}/followers")
    @Produces(VndType.APPLICATION_FOLLOWED_LISTS)
    public Response getUsersWhoFollowList(@PathParam("listId") final int listId,
            @QueryParam("page") @DefaultValue("0") final int page) {

        List<User> followedUsers = moovieListService.usersFollowsMoovieList(listId, page,
                PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize());
        if (followedUsers.isEmpty()) {
            return Response.noContent().build();
        }
        List<UserListIdDto> toRet = new ArrayList<>();
        for (User user : followedUsers) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("lists")
                    .path(String.valueOf(listId))
                    .path("followers")
                    .path(user.getUsername())
                    .build().toString();
            toRet.add(new UserListIdDto(listId, user.getUsername(), uri, uriInfo));
        }
        return Response.ok(new GenericEntity<List<UserListIdDto>>(toRet) {
        }).build();
    }

    @GET
    @Path("{id}/recommendedLists")
    @Produces(VndType.APPLICATION_MOOVIELIST_LIST)
    public Response getRecommendedLists(@PathParam("id") final int id) {
        try {
            List<MoovieListDto> mlcList = MoovieListDto
                    .fromMoovieListList(moovieListService.getRecommendedMoovieListCards(id, 4, 0), uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListDto>>(mlcList) {
            });
            return res.build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred.")).build();
        }
    }

    // We have a separate endpoint for content to be able to use filters and no need
    // to do it every time we want to find a list
    // PROBLEM WHEN SORT ORDER AND OR ORDER BY ARE NULL
    @GET
    @Path("/{id}/content")
    @Produces(VndType.APPLICATION_MOOVIELIST_MEDIA_LIST)
    public Response getMoovieListMedia(@PathParam("id") final int id,
            @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
            @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
            @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
            @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {
            MoovieListCard mlc = moovieListService.getMoovieListCardById(id);
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize()) {
                pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
            }

            List<OrderedMedia> mediaList = moovieListService.getMoovieListContentOrdered(id, orderBy, sortOrder,
                    pageSizeQuery, pageNumber);
            final int mediaCount = mlc.getSize();
            List<MediaIdListIdDto> dtoList = MediaIdListIdDto.fromOrderedMediaList(mediaList, id, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MediaIdListIdDto>>(dtoList) {
            });
            final PagingUtils<MediaIdListIdDto> toReturnMediaIdListId = new PagingUtils<>(dtoList, pageNumber,
                    pageSizeQuery, mediaCount);
            ResponseUtils.setPaginationLinks(res, toReturnMediaIdListId, uriInfo);
            return res.build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource.")).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred.")).build();
        }

    }

    @POST
    @Path("/{id}/content")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_MOOVIELIST_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMediaIntoMoovieList(@PathParam("id") int id,
            @Valid MediaListDto mediaIdListDto) {

        try {
            moovieListService.getMoovieListCardById(id);
            List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
            MoovieList updatedList = moovieListService.insertMediaIntoMoovieList(id, mediaIdList);
            if (mediaIdList == null || mediaIdList.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ResponseMessage("No media IDs provided."))
                        .build();
            }
            return Response.ok(updatedList).entity(new ResponseMessage("Media added successfully to the list."))
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

    @GET
    @Path("/{id}/content/{mediaId}")
    @Produces(VndType.APPLICATION_MOOVIELIST_MEDIA)
    public Response getMoovieListMediaByMediaId(@PathParam("id") final int id,
            @PathParam("mediaId") final int mediaId) {
        try {
            moovieListService.getMoovieListCardById(id);

            int customOrder = moovieListService.isMediaInMoovieList(mediaId, id);
            if (customOrder == -1) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseMessage("Media not found in MoovieList."))
                        .build();
            }
            return Response.ok(new MediaIdListIdDto(mediaId, id, customOrder, uriInfo)).build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

    @PUT
    @Path("/{id}/content/{mediaId}")
    @PreAuthorize("@accessValidator.isUserListAuthor(#id)")
    @Consumes(VndType.APPLICATION_MOOVIELIST_MEDIA_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMoovieListMediaByMediaId(@PathParam("id") final int id,
            @PathParam("mediaId") final int mediaId,
            final MediaIdListIdDto input) {
        try {
            moovieListService.getMoovieListCardById(id);

            moovieListService.updateMoovieListOrder(input.getMoovieListId(), input.getMediaId(),
                    input.getCustomOrder());
            return Response.ok()
                    .entity("MoovieList order succesfully modified.").build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

    @DELETE
    @Path("/{id}/content/{mediaId}")
    @PreAuthorize("@accessValidator.isUserListAuthor(#id)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMediaMoovieList(@PathParam("id") final int id, @PathParam("mediaId") final int mId) {
        try {
            moovieListService.deleteMediaFromMoovieList(id, mId);
            return Response.noContent().build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ResponseMessage("You do not have access to this resource."))
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("MoovieList not found."))
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ResponseMessage("An unexpected error occurred."))
                    .build();
        }

    }

}
