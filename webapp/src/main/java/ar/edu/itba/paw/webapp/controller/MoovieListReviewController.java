package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToFindUserException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.MoovieListReviewReport;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.MoovieListReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.MoovieListReviewDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("moovieListReviews")
@Component
public class MoovieListReviewController {
    private final ReviewService reviewService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListReviewById(@PathParam("id") int id) {
        try {
            final MoovieListReview moovieListReview = reviewService.getMoovieListReviewById(id);
            final MoovieListReviewDto moovieListReviewDto = MoovieListReviewDto.fromMoovieListReview(moovieListReview, uriInfo);
            return Response.ok(moovieListReviewDto).build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoovieListReviewsFromQueryParams(
            @QueryParam("listId") final Integer listId,
            @QueryParam("userId") final Integer userId,
            @QueryParam("pageNumber") @DefaultValue("1") final int page) {

        try {
            if (listId != null) {
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsByMoovieListId(
                        listId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final int moovieListReviewsCount = reviewService.getMoovieListReviewByMoovieListIdCount(listId);
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                });
                final PagingUtils<MoovieListReview> toReturnMoovieListReviews = new PagingUtils<>(moovieListReviews, page - 1, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), moovieListReviewsCount);
                ResponseUtils.setPaginationLinks(res, toReturnMoovieListReviews, uriInfo);
                return res.build();

            } else if (userId != null) {
                final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsFromUser(
                        userId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
                final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
                return Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {
                }).build();

            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Either listId or userId must be provided.").build();
            }
        } catch (UnableToFindUserException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@QueryParam("listId") int listId,
                               @Valid final MoovieListReviewCreateDto moovieListReviewDto) {
        reviewService.editReview(
                listId,
                0,
                moovieListReviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MOOVIE_LIST
        );

        return Response.ok()
                .entity("MoovieList review successfully updated for MoovieList with ID: " + listId)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMoovieListReview(@QueryParam("listId") int listId,
                                                            @Valid final MoovieListReviewCreateDto moovieListReviewDto) {
        reviewService.createReview(
                listId,
                0,
                moovieListReviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MOOVIE_LIST
        );

        return Response.status(Response.Status.CREATED)
                .entity("MoovieList review successfully created to the list with ID: " + listId)
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMoovieListReviewById(@PathParam("id") final int moovieListReview) {
        try {
            reviewService.deleteReview(moovieListReview, ReviewTypes.REVIEW_MOOVIE_LIST);

            return Response.ok()
                    .entity("Review successfully deleted.")
                    .build();

        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found or you do not have permission to delete.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found or you do not have permission to delete.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }



    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeMoovieListReview(@PathParam("id") final int id) {
        try {
            boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MOOVIE_LIST);
            if (liked) {
                return Response.ok()
                        .entity("MoovieList review successfully liked.")
                        .build();
            }
            return Response.ok()
                    .entity("MoovieList review successfully unliked.")
                    .build();

        } catch (UserNotLoggedException | UnableToFindUserException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to like a review.\"}")
                    .build();
        } catch (ReviewNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList review not found.\"}")
                    .build();
        } catch (MoovieListNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"MoovieList not found or you do not have permission to delete.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

}






