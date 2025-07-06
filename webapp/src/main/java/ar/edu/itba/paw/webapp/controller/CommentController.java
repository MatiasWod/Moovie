package ar.edu.itba.paw.webapp.controller;

import java.util.List;
import java.util.function.Supplier;

import javax.persistence.NoResultException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedbackType;
import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.CommentService;
import ar.edu.itba.paw.services.ReportService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.CommentCreateDto;
import ar.edu.itba.paw.webapp.dto.in.CommentFeedbackDto;
import ar.edu.itba.paw.webapp.dto.out.CommentDto;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.vndTypes.VndType;

@Path("comments")
@Component
public class CommentController {
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final ReportService reportService;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Context
    UriInfo uriInfo;

    @Autowired
    public CommentController(CommentService commentService, ReviewService reviewService, ReportService reportService,
            UserService userService) {
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.reportService = reportService;
        this.userService = userService;
    }

    @GET
    @Produces(VndType.APPLICATION_COMMENT_LIST)
    public Response getCommentsByReviewId(@QueryParam("reviewId") final Integer reviewId,
            @QueryParam("isReported") final boolean isReported,
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
                    logger.info("isReported");
                    final int commentCount = reportService.getReportedCommentsCount();
                    int pageSize = PagingSizes.REPORT_DEFAULT_PAGE_SIZE.getSize();
                    final List<Comment> commentList = reportService
                            .getReportedComments(pageSize, page);
                    final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

                    Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
                    });
                    final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page,
                            pageSize, commentCount);
                    ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                    return res.build();
                } catch (UserNotLoggedException e) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(e.getMessage())
                            .build();
                }
            } else if (reviewId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Review id is required if not filtering by reported")
                        .build();
            }
            final int commentCount = reviewService.getReviewById(reviewId).getCommentCount().intValue();
            final List<Comment> commentList = commentService.getComments(reviewId,
                    PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page - 1);
            final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);

            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
            });
            final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page,
                    PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return res.build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Review not found")
                    .build();
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_COMMENT)
    public Response getCommentById(@PathParam("id") @NotNull int id, @Context Request request) {
        try {
            final Comment comment = commentService.getCommentById(id);
            if (comment == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Comment not found")
                        .build();
            }
            final Supplier<CommentDto> dtoSupplier = () -> CommentDto.fromComment(comment, uriInfo);
            return ResponseUtils.setConditionalCacheHash(request, dtoSupplier, comment.hashCode());
        } catch (RuntimeException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({ VndType.APPLICATION_COMMENT_FORM })
    public Response createComment(@Valid @NotNull final CommentCreateDto commentDto) {
        try {
            commentService.createComment(
                    commentDto.getReviewId(),
                    commentDto.getCommentContent());
            return Response.status(Response.Status.CREATED)
                    .entity("Comment successfully created to review with id:" + commentDto.getReviewId())
                    .build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to create a comment.\"}")
                    .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Review not found")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Consumes({ VndType.APPLICATION_COMMENT_FEEDBACK_FORM })
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFeedbackOnComment(@PathParam("id") @NotNull int id,
            @Valid @NotNull final CommentFeedbackDto commentFeedbackDto) {
        try {
            Comment comment = commentService.getCommentById(id);
            if (comment == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Comment not found")
                        .build();
            }
            CommentFeedbackType commentFeedbackType = commentFeedbackDto.transformToEnum();
            if (commentFeedbackType == CommentFeedbackType.LIKE) {
                boolean liked = commentService.likeComment(id);
                if (!liked) {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to unliked")
                            .build();
                } else {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to liked")
                            .build();
                }
            } else if (commentFeedbackType == CommentFeedbackType.DISLIKE) {
                boolean disliked = commentService.dislikeComment(id);
                if (!disliked) {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to undisliked")
                            .build();
                } else {
                    return Response.ok()
                            .entity("Comment feedback status successfully changed to disliked")
                            .build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid feedback type")
                        .build();
            }
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to update a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }

    }

    @DELETE
    @Path("/{id}")
    @PreAuthorize("@accessValidator.isUserLoggedIn() or @accessValidator.isUserAdmin()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteComment(@PathParam("id") @NotNull int id) {
        try {
            if (commentService.getCommentById(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Comment not found")
                        .build();
            }
            commentService.deleteComment(id);
            return Response.ok().entity("Comment deleted").build();
        } catch (AccessDeniedException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"User is not the author of the comment.\"}")
                    .build();
        } catch (UserNotLoggedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"User must be logged in to delete a comment.\"}")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

}
