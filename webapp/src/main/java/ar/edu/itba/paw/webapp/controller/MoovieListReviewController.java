package ar.edu.itba.paw.webapp.controller;

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

import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.webapp.dto.in.MoovieListReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exceptions.InvalidAccessToResourceException;
import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.ReviewAlreadyCreatedException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewDto;
import java.util.ArrayList;


@Path("moovieListReviews")
@Component
public class MoovieListReviewController {
    private final ReviewService reviewService;
    private final MoovieListService moovieListService;
    private final ReportService reportService;
    private final UserService userService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListReviewController(ReviewService reviewService, MoovieListService moovieListService,
            ReportService reportService, UserService userService) {
        this.reviewService = reviewService;
        this.moovieListService = moovieListService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW)
    public Response getMoovieListReviewById(@PathParam("id") @NotNull int id, @Context Request request) {
        try {
            final MoovieListReview moovieListReview = reviewService.getMoovieListReviewById(id);
            final Supplier<MoovieListReviewDto> dtoSupplier = () -> MoovieListReviewDto
                    .fromMoovieListReview(moovieListReview, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, moovieListReview.hashCode());
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_LIST)
    public Response getMoovieListReviewsFromQueryParams(
            @QueryParam("listId") final Integer listId,
            @QueryParam("username") final String username,
            @QueryParam("isReported") final boolean isReported,
            @QueryParam("likedByUser") final String likedByUser,
            @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            if (isReported) {
                try {
                    User user = userService.getInfoOfMyUser();
                    if (user.getRole() < UserRoles.MODERATOR.getRole()) {
                        return Response.status(Response.Status.FORBIDDEN)
                                .entity("User is not moderator")
                                .build();
                    }
                    int pageSize = PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize();
                    final List<MoovieListReview> moovieListReviews = reportService
                            .getReportedMoovieListReviews(pageSize, page);
                    final int moovieListReviewsCount = reportService.getReportedMoovieListReviewsCount();
                    final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto
                            .fromMoovieListReviewList(moovieListReviews, uriInfo);
                    Response.ResponseBuilder res = Response
                            .ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                            });
                    final PagingUtils<MoovieListReview> toReturnMoovieListReviews = new PagingUtils<>(moovieListReviews,
                            page - 1, pageSize, moovieListReviewsCount);
                    ResponseUtils.setPaginationLinks(res, toReturnMoovieListReviews, uriInfo);
                    return res.build();
                } catch (UserNotLoggedException e) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(e.getMessage())
                            .build();
                }
            }
            if(likedByUser != null) {
                final List<MoovieListReview> mlReviews = reviewService.getLikedMoovieListReviewsByUser(likedByUser, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final int mlReviewsCount = reviewService.getLikedMoovieListReviewsCountByUser(likedByUser);
                final List<MoovieListReviewDto> mlReviewsDto = MoovieListReviewDto.fromMoovieListReviewList(mlReviews, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListReviewDto>>(mlReviewsDto) {
                });
                final PagingUtils<MoovieListReview> reviewPagingUtils = new PagingUtils<>(mlReviews, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), mlReviewsCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            }if (listId != null) {
                // Get MoovieList if it exists
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsByMoovieListId(
                        listId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final int moovieListReviewsCount = reviewService.getMoovieListReviewByMoovieListIdCount(listId);
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto
                        .fromMoovieListReviewList(moovieListReviews, uriInfo);
                Response.ResponseBuilder res = Response
                        .ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                        });
                final PagingUtils<MoovieListReview> toReturnMoovieListReviews = new PagingUtils<>(moovieListReviews,
                        page - 1, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), moovieListReviewsCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListReviews, uriInfo);
                return res.build();

            } else if (username != null) {
                User user = userService.findUserByUsername(username);
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsFromUser(
                        user.getUserId(), PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final int reviewCount = reviewService.getMoovieListReviewsFromUserCount(user.getUserId());
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto
                        .fromMoovieListReviewList(moovieListReviews, uriInfo);

                Response.ResponseBuilder res = Response
                        .ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                        });
                final PagingUtils<MoovieListReview> reviewPagingUtils = new PagingUtils<>(moovieListReviews, page,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();

            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Either listId or userId must be provided.")
                        .build();
            }
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"User not found.\"}").build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({ VndType.APPLICATION_MOOVIELIST_REVIEW_FORM })
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieListReview(@Valid @NotNull final MoovieListReviewCreateDto moovieListReviewDto) {
        try {
            moovieListService.getMoovieListById(moovieListReviewDto.getListId());

            int reviewId = reviewService.createReview(
                    moovieListReviewDto.getListId(),
                    0,
                    moovieListReviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MOOVIE_LIST);

            return Response.created(uriInfo
                            .getBaseUriBuilder()
                            .path("moovieListReviews")
                            .path(String.valueOf(reviewId))
                            .build())
                    .build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission to create a review for this MoovieList.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        } catch (ReviewAlreadyCreatedException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Review already created for this MoovieList.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({ VndType.APPLICATION_MOOVIELIST_REVIEW_FORM })
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") @NotNull final int id,
            @Valid @NotNull final MoovieListReviewCreateDto moovieListReviewDto) {
        try {
            moovieListService.getMoovieListById(moovieListReviewDto.getListId());
            reviewService.editReview(
                    moovieListReviewDto.getListId(),
                    0,
                    moovieListReviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MOOVIE_LIST);
            return Response.ok()
                    .entity("MoovieList review successfully updated for MoovieList with ID: "
                            + moovieListReviewDto.getListId())
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission, list is private.\"}")
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
    public Response deleteMoovieListReviewById(@PathParam("id") final int id) {
        try {
            reviewService.deleteReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);

            return Response.ok()
                    .entity("Review successfully deleted.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a review.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (InvalidAccessToResourceException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"You do not have permission to delete this review.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    // Likes

    @POST
    @Path("/{id}/likes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({VndType.APPLICATION_MOOVIELIST_REVIEW_LIKE})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReviewLike(@PathParam("id") int id) {
        String currentUser = userService.getInfoOfMyUser().getUsername();
        boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);
        if (liked)
            return Response.created(
                    uriInfo
                            .getBaseUriBuilder()
                            .path("moovieListReviews")
                            .path(String.valueOf(id))
                            .path("likes")
                            .path(currentUser)
                            .build()
            ).build();
        return Response.status(Response.Status.CONFLICT)
                .entity("{\"message\":\"List is already liked.\"}").build();
    }

    @DELETE
    @Path("/{id}/likes/{username}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReviewLike(@PathParam("id") int id,
                                     @PathParam("username") String username) {
        try{
            User user = userService.getInfoOfMyUser();
            if (!user.getUsername().equals(username)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"message\":\"You can only remove your own likes.\"}")
                        .build();
            }
        boolean removed = reviewService.removeLikeReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);
        if (removed)
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"List review is not liked.\"}").build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(e.getMessage()).build();
        }
    }

    // Returns like status for a specific review for a user
    @GET
    @Path("/{id}/likes/{username}")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_LIKE)
    public Response getUserLikedListById(@PathParam("id") final int reviewId,
                                         @PathParam("username") final String username) {
        if (reviewService.userLikesReview(reviewId, username, ReviewTypes.REVIEW_MOOVIE_LIST)) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("moovieListReviews")
                    .path(String.valueOf(reviewId))
                    .path("likes")
                    .path(username)
                    .build().toString();
            return Response.ok(new UserMoovieListReviewDto(reviewId, username, uri, uriInfo)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User has not liked the Moovie List Review").build();
    }

    // Return all likes for a review
    @GET
    @Path("/{id}/likes")
    @Produces(VndType.APPLICATION_MOOVIELIST_REVIEW_LIKE_LIST) // Asumiendo un VndType para listas de usuarios
    public Response getUsersWhoLikedList(@PathParam("id") final int reviewId,
                                         @QueryParam("pageNumber") @DefaultValue("1") final int page) {

        List<User> likedUsers = reviewService.usersLikesReview(reviewId, page, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), ReviewTypes.REVIEW_MOOVIE_LIST );
        if (likedUsers.isEmpty()) {
            return Response.noContent().build();
        }

        int totalCount = reviewService.getLikedReviewsCountByReviewId(reviewId, ReviewTypes.REVIEW_MOOVIE_LIST);

        List<UserMoovieListReviewDto> toRet =  new ArrayList<>();
        for (User user : likedUsers) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("moovieListReviews")
                    .path(String.valueOf(reviewId))
                    .path("likes")
                    .path(user.getUsername())
                    .build().toString();
            toRet.add(new UserMoovieListReviewDto(reviewId, user.getUsername(), uri, uriInfo));
        }

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserMoovieListReviewDto>>(toRet) {
        });
        final PagingUtils<UserMoovieListReviewDto> pagingUtils = new PagingUtils<>(toRet, page, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), totalCount);
        ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
        return res.build();
    }




}
