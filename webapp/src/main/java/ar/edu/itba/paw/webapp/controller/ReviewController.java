package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Reports.ReviewReport;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.*;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.ReportCreateDTO;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.dto.out.ReportDTO;
import ar.edu.itba.paw.webapp.dto.out.ReviewDto;
import ar.edu.itba.paw.webapp.mappers.ExceptionEM;
import ar.edu.itba.paw.webapp.mappers.InvalidAccessToResourceEM;
import ar.edu.itba.paw.webapp.mappers.UnableToFindUserEM;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
//TODO CHECK LOGGERS
//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;


@Path("review")
@Component
public class ReviewController {
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final ReportService reportService;
    private final UserService userService;

    @Context
    UriInfo uriInfo;

    //private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService, CommentService commentService, ReportService reportService, UserService userService) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.reportService = reportService;
        this.userService = userService;
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviewById(@PathParam("id") final int id) {
        final Review review = reviewService.getReviewById(id);
        final ReviewDto reviewDto = ReviewDto.fromReview(review, uriInfo);
        return Response.ok(reviewDto).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReviewById(@PathParam("id") final int reviewId) {
        reviewService.deleteReview(reviewId, ReviewTypes.REVIEW_MEDIA);

        return Response.ok()
                .entity("Review successfully deleted.")
                .build();

    }

    @POST
    @Path("/{id}/like")
    @Produces(MediaType.APPLICATION_JSON)
    public Response likeReview(@PathParam("id") final int id) {
        boolean liked = reviewService.likeReview(id, ReviewTypes.REVIEW_MEDIA);
        if (liked) {
            return Response.ok()
                    .entity("Review successfully liked.")
                    .build();
        }
        return Response.ok()
                .entity("Review successfully unliked.")
                .build();
    }

    /* COMMENTS */
    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("id") final int id, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        final int commentCount = reviewService.getReviewById(id).getCommentCount().intValue();
        final List<Comment> commentList = commentService.getComments(id, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
        final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

        Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
        });
        final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
        ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
        return res.build();
    }

    @POST
    @Path("/{id}/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") final int id, @Valid final CommentCreateDto commentDto) {
        commentService.createComment(
                id,
                commentDto.getCommentContent()
        );
        return Response.status(Response.Status.CREATED)
                .entity("Comment successfully created to review with id:" + id)
                .build();
    }


//    --------Moderation-------

    @POST
    @Path("/{id}/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reportReview(@PathParam("id") final int id,
                                 @Valid final ReportCreateDTO reportDTO) {
        LOGGER.info("----------------REPORTING A REVIEW ------------------");
        try {
            User reportingUser = userService.findUserByUsername(reportDTO.getReportedBy());
            User currentUser = userService.getInfoOfMyUser();
            if (reportingUser.getUserId() != currentUser.getUserId()) {
                return new InvalidAccessToResourceEM().toResponse(new InvalidAccessToResourceException("The report indicates a different user as the reporter."));
            }
            ReviewReport response = reportService.reportReview(id, reportingUser.getUserId(), reportDTO.getType(), reportDTO.getContent());
            return Response.ok(ReportDTO.fromReviewReport(response, uriInfo)).build();
        } catch (UnableToFindUserException e) {
            return new UnableToFindUserEM().toResponse(e);
        } catch (Exception e) {
            return new ExceptionEM().toResponse(e);
        }

    }

}

