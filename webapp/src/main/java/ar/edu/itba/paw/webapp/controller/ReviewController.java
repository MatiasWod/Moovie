package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.function.Supplier;

import javax.validation.Valid;
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

import ar.edu.itba.paw.webapp.dto.out.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.ReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.dto.out.UserReviewDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import java.util.ArrayList;



//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

@Api(value = "/reviews")
@Path("reviews")
@Component
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final ReportService reportService;

    @Context
    UriInfo uriInfo;

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService, ReportService reportService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.reportService = reportService;
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_REVIEW)
    public Response getReviewById(@PathParam("id") final int id, @Context Request request) {
        final Review review = reviewService.getReviewById(id);
        final Supplier<ReviewDto> dtoSupplier = () -> ReviewDto.fromReview(review, uriInfo);
        return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, review.hashCode());
    }

    @GET
    @Produces(VndType.APPLICATION_REVIEW_LIST)
    public Response getReviewsByQueryParams(
            @QueryParam("mediaId") final Integer mediaId,
            @QueryParam("username") final String username,
            @QueryParam("isReported") final boolean isReported,
            @QueryParam("likedByUser") final String likedByUser,
            @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        if (isReported) {
            try {
                User user = userService.getInfoOfMyUser();
                if (user.getRole() < UserRoles.MODERATOR.getRole()) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity(new ResponseMessage("User is not moderator"))
                            .build();
                }
                int pageSize = PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize();
                final List<Review> reviews = reportService
                        .getReportedReviews(pageSize, page);
                final int reviewCount = reportService.getReportedReviewsCount();
                final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {
                });
                final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page,
                        pageSize, reviewCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            } catch (UserNotLoggedException e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(e.getMessage())
                        .build();
            }
        }
        // Damos todos los likes para un usuario
        if(likedByUser != null){
            final List<Review> reviews = reviewService.getLikedReviewsByUser(likedByUser, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            final int reviewCount = reviewService.getLikedReviewsCountByUser(likedByUser);
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {});
            final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return  res.build();
        }
        if (mediaId != null && username != null) {
            // Caso: buscar una reseña específica por mediaId y username
            User user = userService.findUserByUsername(username);
            final Review review = reviewService.getReviewByMediaIdAndUsername(mediaId, user.getUserId());
            if (review == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseMessage("Review not found."))
                        .build();
            }
            final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
            return Response.ok(reviewDto).build();
        } else if (mediaId != null) {
            // Caso: buscar todas las reseñas para un mediaId con paginación
            final List<Review> reviews = reviewService.getReviewsByMediaId(mediaId,
                    PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
            final int reviewCount = reviewService.getReviewsByMediaIdCount(mediaId);
            final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {
            });
            final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page,
                    PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return res.build();
        } else if (username != null) {
            try {
                final List<Review> reviews = reviewService.getMovieReviewsFromUser(
                        userService.findUserByUsername(username).getUserId(),
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
                final int reviewCount = userService.findUserByUsername(username).getReviewsCount();

                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {
                });
                final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            } catch (RuntimeException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
            }
        } else {
            // Caso: parámetros inválidos o faltantes
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseMessage("At least one valid parameter is required."))
                    .build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_REVIEW_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReview(@Valid final ReviewCreateDto reviewDto) {
        int reviewId = reviewService.createReview(
                reviewDto.getMediaId(),
                reviewDto.getRating(),
                reviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MEDIA);

        return Response.created(
                        uriInfo.getBaseUriBuilder()
                                .path("reviews")
                                .path(String.valueOf(reviewId))
                                .build()
                )
                .build();
    }

    @PUT
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Path("/{id}")
    @Consumes(VndType.APPLICATION_REVIEW_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") final int id, @Valid final ReviewCreateDto reviewDto) {
        try {
            reviewService.getReviewById(id);
            reviewService.editReview(
                    reviewDto.getMediaId(),
                    reviewDto.getRating(),
                    reviewDto.getReviewContent(),
                    ReviewTypes.REVIEW_MEDIA);

            return Response.ok()
                    .entity(new ResponseMessage("Review successfully updated for media with ID: " + reviewDto.getMediaId()))
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("{\"error\":\"Review not found.\"}"))
                    .build();
        }
    }



    @DELETE
    @PreAuthorize("@accessValidator.isUserReviewAuthor(#reviewId) or @accessValidator.isUserAdmin()")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReviewById(@PathParam("id") final int reviewId) {
        reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);

        return Response.ok()
                .entity(new ResponseMessage("Review successfully deleted."))
                .build();

    }



    // Likes

    @POST
    @Path("/{id}/likes")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReviewLike(@PathParam("id") int id) {
        String username = userService.getInfoOfMyUser().getUsername();
        boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MEDIA);
        if (liked)
            return Response.created(uriInfo.getBaseUriBuilder()
                    .path("reviews")
                    .path(String.valueOf(id))
                    .path("likes")
                    .path(username)
                    .build()).build();
        return Response.status(Response.Status.CONFLICT)
                .entity(new ResponseMessage("{\"message\":\"List is already liked.\"}")).build();
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
                        .entity(new ResponseMessage("{\"message\":\"You can only remove your own likes.\"}"))
                        .build();
            }
            boolean removed = reviewService.removeLikeReview(id, ReviewTypes.REVIEW_MEDIA);
            if (removed)
                return Response.noContent().build();
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseMessage("{\"message\":\"List is not liked.\"}")).build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ResponseMessage(e.getMessage())).build();
        }
    }

    // Returns like status for a specific review for a user
    @GET
    @Path("/{id}/likes/{username}")
    @Produces(VndType.APPLICATION_REVIEW_LIKE)
    public Response getUserLikedListById(@PathParam("id") final int reviewId,
                                         @PathParam("username") final String username) {
        if (reviewService.userLikesReview(reviewId, username, ReviewTypes.REVIEW_MEDIA)) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("reviews")
                    .path(String.valueOf(reviewId))
                    .path("likes")
                    .path(username)
                    .build().toString();
            return Response.ok(new UserReviewDto(reviewId, username, uri, uriInfo)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("User has not liked the review.")).build();
    }

    // Return all likes for a review
    @GET
    @Path("/{id}/likes")
    @Produces(VndType.APPLICATION_REVIEW_LIKE_LIST) // Asumiendo un VndType para listas de usuarios
    public Response getUsersWhoLikedList(@PathParam("id") final int reviewId,
                                         @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        List<User> likedUsers = reviewService.usersLikesReview(reviewId, page-1, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), ReviewTypes.REVIEW_MEDIA );
        if (likedUsers.isEmpty()) {
            return Response.noContent().build();
        }

        int totalCount = reviewService.getLikedReviewsCountByReviewId(reviewId, ReviewTypes.REVIEW_MEDIA);

        List<UserReviewDto> toRet =  new ArrayList<>();
        for (User user : likedUsers) {
            final String uri = uriInfo.getBaseUriBuilder()
                    .path("reviews")
                    .path(String.valueOf(reviewId))
                    .path("likes")
                    .path(user.getUsername())
                    .build().toString();
            toRet.add(new UserReviewDto(reviewId, user.getUsername(), uri, uriInfo));
        }
        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserReviewDto>>(toRet) {});
        final PagingUtils<UserReviewDto> pagingUtils = new PagingUtils<>(toRet, page, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), totalCount);
        ResponseUtils.setPaginationLinks(res, pagingUtils, uriInfo);
        return res.build();
    }

}
