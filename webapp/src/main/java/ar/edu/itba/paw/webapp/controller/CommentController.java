package ar.edu.itba.paw.webapp.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

import ar.edu.itba.paw.exceptions.ConflictException;
import ar.edu.itba.paw.exceptions.ForbiddenException;
import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotLoggedException;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.Comments.CommentFeedback;
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
import ar.edu.itba.paw.webapp.dto.out.UserCommentFeedbackDto;
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
            @QueryParam("feebackedBy") final String feedbackedBy,
            @QueryParam("username") final String username,
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
            } else if (feedbackedBy != null) {
                final int commentCount = commentService.getCommentFeedbackForUserCount(feedbackedBy);
                final List<Comment> commentList = commentService.getCommentFeedbackForUser(feedbackedBy, page - 1,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
                final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
                });
                final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
            } else if (username != null) {
                User user = userService.findUserByUsername(username);
                final int commentCount = user.getCommentsCount();
                final List<Comment> commentList = commentService.getCommentsForUsername(user.getUserId(), page - 1,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
                final List<CommentDto> commentDtoList = CommentDto.fromCommentList(commentList, uriInfo);
                Response.ResponseBuilder res = Response.ok(new GenericEntity<List<CommentDto>>(commentDtoList) {
                });
                final PagingUtils<Comment> reviewPagingUtils = new PagingUtils<>(commentList, page,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), commentCount);
                ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
                return res.build();
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
            int createdID = commentService.createComment(
                    commentDto.getReviewId(),
                    commentDto.getCommentContent());

            final URI uri = uriInfo.getBaseUriBuilder()
                    .path("comments")
                    .path(String.valueOf(createdID))
                    .build();

            return Response.created(uri)
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

    @POST
    @Path("/{id}/feedback")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFeedback(@PathParam("id") int id,
            @Valid @NotNull final CommentFeedbackDto commentFeedbackDto) {
        Comment comment = commentService.getCommentById(id);
        CommentFeedbackType commentFeedbackType = commentFeedbackDto.transformToEnum();
        int uid = userService.getInfoOfMyUser().getUserId();
        String username = userService.getInfoOfMyUser().getUsername();
        if (commentService.userHasLiked(id, uid) || commentService.userHasDisliked(id, uid)) {
            throw new ConflictException("User already has feedback on this list.");
        }
        final URI uri = uriInfo.getBaseUriBuilder()
                .path("comments")
                .path(String.valueOf(id))
                .path("feedback")
                .path(username)
                .build();
        if (commentFeedbackType == CommentFeedbackType.LIKE) {
            commentService.likeComment(id);
            return Response.created(uri).build();
        }
        if (commentFeedbackType == CommentFeedbackType.DISLIKE) {
            commentService.dislikeComment(id);
            return Response.created(uri).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid feedback type")
                .build();
    }

    @PUT
    @Path("/{id}/feedback/{username}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeFeedback(@PathParam("id") int id,
            @PathParam("username") String username,
            @Valid @NotNull final CommentFeedbackDto commentFeedbackDto) {
        Comment comment = commentService.getCommentById(id);
        CommentFeedbackType commentFeedbackType = commentFeedbackDto.transformToEnum();
        int uid = userService.getInfoOfMyUser().getUserId();
        String currentUsername = userService.getInfoOfMyUser().getUsername();
        if (!Objects.equals(username, currentUsername)) {
            throw new ForbiddenException("You are not allowed to modify this comment feedback.");
        }
        boolean existed = false;
        if (commentService.userHasLiked(id, uid) || commentService.userHasDisliked(id, uid)) {
            existed = true; // If resource already existed then 200 if not (we create and) 201.
        }
        final URI uri = uriInfo.getBaseUriBuilder()
                .path("comments")
                .path(String.valueOf(id))
                .path("feedback")
                .path(username)
                .build();
        if (commentFeedbackType == CommentFeedbackType.LIKE) {
            commentService.likeComment(id);
            if (!existed) {
                return Response.created(uri).build();
            }
            return Response.ok(new UserCommentFeedbackDto(id, username, uri.toString(), "LIKE", uriInfo))
                    .build();
        }
        if (commentFeedbackType == CommentFeedbackType.DISLIKE) {
            commentService.dislikeComment(id);
            if (!existed) {
                return Response.created(uri).build();
            }
            return Response.ok(new UserCommentFeedbackDto(id, username, uri.toString(), "DISLIKE", uriInfo))
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid feedback type")
                .build();
    }

    @DELETE
    @Path("/{id}/feedback/{username}")
    @PreAuthorize("@accessValidator.isUserLoggedIn()")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCommentFeedbackLike(@PathParam("id") int id, @PathParam("username") String username) {
        Comment comment = commentService.getCommentById(id);
        String currentUsername = userService.getInfoOfMyUser().getUsername();
        if (!Objects.equals(username, currentUsername)) {
            throw new ForbiddenException("You are not allowed to delete this comment feedback.");
        }
        int uid = userService.getInfoOfMyUser().getUserId();
        if (commentService.userHasLiked(id, uid) || commentService.userHasDisliked(id, uid)) {
            commentService.removeDislikeComment(id);
            commentService.removeLikeComment(id);

            return Response.noContent().build();
        }
        throw new ResourceNotFoundException("No feedback for comment with id and username given.");
    }

    // Returns like status for a specific review for a user
    @GET
    @Path("/{id}/feedback/{username}")
    @Produces(VndType.APPLICATION_COMMENT_FEEDBACK)
    public Response getCommentFeedbackByUsername(@PathParam("id") final int id,
            @PathParam("username") final String username) {
        int uid = userService.findUserByUsername(username).getUserId();
        boolean hasLiked = commentService.userHasLiked(id, uid);
        boolean hasDisliked = commentService.userHasDisliked(id, uid);
        final URI uri = uriInfo.getBaseUriBuilder()
                .path("comments")
                .path(String.valueOf(id))
                .path("feedback")
                .path(username)
                .build();

        if (hasLiked) {
            return Response.ok(new UserCommentFeedbackDto(id, username, uri.toString(), "LIKE", uriInfo))
                    .build();
        }
        if (hasDisliked) {
            return Response.ok(new UserCommentFeedbackDto(id, username, uri.toString(), "DISLIKE", uriInfo))
                    .build();
        }
        throw new ResourceNotFoundException("No feedback for comment with id and username given.");
    }

    // Return all likes for a review
    @GET
    @Path("/{id}/feedback")
    @Produces(VndType.APPLICATION_COMMENT_FEEDBACK_LIST)
    public Response getUsersWhoGaveFeedbackToComment(@PathParam("id") final int id,
            @QueryParam("pageNumber") @DefaultValue("1") final int page,
            @QueryParam("type") final String feedback) {
        try{
            List<CommentFeedback> usersFeedback;
            int totalCount = 0;
            if (feedback != null) {
                CommentFeedbackType feedbackType = CommentFeedbackType.valueOf(feedback);
                logger.info("Received comment feedback type {}", feedbackType);
                    usersFeedback = commentService.getCommentFeedbackForComment(id, feedbackType, page - 1,
                            PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
                    totalCount = commentService.getCommentFeedbackForCommentCount(id, feedbackType);
                logger.info("Total count {}; usersFeedback size {}", totalCount, usersFeedback.size());
            } else{
                usersFeedback = commentService.getCommentFeedbackForComment(id, page - 1,
                        PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize());
                totalCount = commentService.getCommentFeedbackForCommentCount(id);
            }
            if (usersFeedback.isEmpty() || totalCount == 0) {
                return Response.noContent().build();
            }

            List<UserCommentFeedbackDto> toRet = new ArrayList<>();
            for (CommentFeedback commentFeedback : usersFeedback) {
                toRet.add(UserCommentFeedbackDto.fromCommentFeedback(commentFeedback, uriInfo));
            }
            Response.ResponseBuilder res = Response.ok(new GenericEntity<List<UserCommentFeedbackDto>>(toRet) {
            });
            final PagingUtils<UserCommentFeedbackDto> reviewPagingUtils = new PagingUtils<>(toRet, page,
                    PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), totalCount);
            ResponseUtils.setPaginationLinks(res, reviewPagingUtils, uriInfo);
            return res.build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid feedback type. Only LIKE and DISLIKE are valid feedback types.").build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("There was an unexpected error.").build();
        }

    }

}
