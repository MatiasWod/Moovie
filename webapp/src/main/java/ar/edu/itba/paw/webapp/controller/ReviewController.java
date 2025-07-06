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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
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
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

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
            @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        if (isReported) {
            try {
                User user = userService.getInfoOfMyUser();
                if (user.getRole() < UserRoles.MODERATOR.getRole()) {
                    return Response.status(Response.Status.FORBIDDEN)
                            .entity("User is not moderator")
                            .build();
                }
                final List<Review> reviews = reportService
                        .getReportedReviews(PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize(), page);
                final int reviewCount = reportService.getReportedReviewsCount();
                final List<ReviewDto> reviewDtos = ReviewDto.fromReviewList(reviews, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<ReviewDto>>(reviewDtos) {
                });
                final PagingUtils<Review> reviewPagingUtils = new PagingUtils<>(reviews, page,
                        PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize(), reviewCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            } catch (UserNotLoggedException e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(e.getMessage())
                        .build();
            }
        }
        if (mediaId != null && username != null) {
            // Caso: buscar una reseña específica por mediaId y userId
            final int userId;
            User user = userService.findUserByUsername(username);
            final Review review = reviewService.getReviewByMediaIdAndUsername(mediaId, user.getUserId());
            if (review == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Review not found.")
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
                    .entity("At least one valid parameter is required.")
                    .build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes(VndType.APPLICATION_REVIEW_FORM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReview(@Valid final ReviewCreateDto reviewDto) {
        reviewService.createReview(
                reviewDto.getMediaId(),
                reviewDto.getRating(),
                reviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MEDIA);

        return Response.status(Response.Status.CREATED)
                .entity("Review successfully created to the media with ID: " + reviewDto.getMediaId())
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
                    .entity("Review successfully updated for media with ID: " + reviewDto.getMediaId())
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response feedbackReview(@PathParam("id") final int id) {
        try {
            boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MEDIA);
            if (liked) {
                return Response.ok()
                        .entity("Review successfully liked.")
                        .build();
            } else {
                return Response.ok()
                        .entity("Review successfully unliked.")
                        .build();
            }
        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Review not found or you do not have permission to delete.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
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
                .entity("Review successfully deleted.")
                .build();

    }

}
