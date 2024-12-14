package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.MoovieList.MoovieList;
import ar.edu.itba.paw.models.MoovieList.MoovieListCard;
import ar.edu.itba.paw.models.MoovieList.MoovieListTypes;
import ar.edu.itba.paw.models.MoovieList.UserMoovieListId;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.PagingUtils;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.services.MoovieListService;
import ar.edu.itba.paw.services.ReviewService;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.in.MediaListDto;
import ar.edu.itba.paw.webapp.dto.in.MoovieListCreateDto;
import ar.edu.itba.paw.webapp.dto.in.MoovieListReviewCreateDto;
import ar.edu.itba.paw.webapp.dto.out.*;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("list")
@Component
public class MoovieListController {

    private final MoovieListService moovieListService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Context
    UriInfo uriInfo;

    @Autowired
    public MoovieListController(MoovieListService moovieListService,ReviewService reviewService, UserService userService) {
        this.moovieListService = moovieListService;
        this.reviewService = reviewService;
        this.userService = userService;
    }


    /**
     * GET METHODS
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getMoovieList(@QueryParam("search") String search,
                                                   @QueryParam("ownerUsername") String ownerUsername,
                                                   @QueryParam("type") @DefaultValue("-1") int type,
                                                   @QueryParam("orderBy") String orderBy,
                                                   @QueryParam("order") String order,
                                                   @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber) {
        try {
             if (type < 1 || type > 4) {
                type = MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType();
            }
            List<MoovieListCard> moovieListCardList = moovieListService.getMoovieListCards(search, ownerUsername, type, orderBy, order, PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(), pageNumber);
            final int moovieListCardCount = moovieListService.getMoovieListCardsCount(search, ownerUsername, type);
            List<MoovieListDto> mlcDto = MoovieListDto.fromMoovieListList(moovieListCardList, uriInfo);
            javax.ws.rs.core.Response.ResponseBuilder res = javax.ws.rs.core.Response.ok(new GenericEntity<List<MoovieListDto>>(mlcDto) {
            });
            final PagingUtils<MoovieListCard> toReturnMoovieListCardList = new PagingUtils<>(moovieListCardList,pageNumber,PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CARDS.getSize(),moovieListCardCount);
            ResponseUtils.setPaginationLinks(res,toReturnMoovieListCardList,uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getMoovieListById(@PathParam("id") final int id) {
        try {
            return javax.ws.rs.core.Response.ok(MoovieListDto.fromMoovieList(moovieListService.getMoovieListCardById(id), uriInfo)).build();
        } catch (Exception e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        }
    }

    //We have a separate endpoint for content to be able to use filters and no need to do it every time we want to find a list
    // PROBLEM WHEN SORT ORDER AND OR ORDER BY ARE NULL
    @GET
    @Path("/{id}/content")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getMoovieListMedia(@PathParam("id") final int id,
                                                        @QueryParam("orderBy") @DefaultValue("customOrder") final String orderBy,
                                                        @QueryParam("sortOrder") @DefaultValue("DESC") final String sortOrder,
                                                        @QueryParam("pageNumber") @DefaultValue("1") final int pageNumber,
                                                        @QueryParam("pageSize") @DefaultValue("-1") final int pageSize) {
        try {
            int pageSizeQuery = pageSize;
            if (pageSize < 1 || pageSize > PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize()) {
                pageSizeQuery = PagingSizes.MOOVIE_LIST_DEFAULT_PAGE_SIZE_CONTENT.getSize();
            }

            List<Media> mediaList = moovieListService.getMoovieListContent(id, orderBy, sortOrder, pageSizeQuery, pageNumber);
            final int mediaCount = moovieListService.getMoovieListCardById(id).getSize();
            List<MediaDto> mediaDtoList = MediaDto.fromMediaList(mediaList, uriInfo);
            javax.ws.rs.core.Response.ResponseBuilder res = javax.ws.rs.core.Response.ok(new GenericEntity<List<MediaDto>>(mediaDtoList) {
            });
            final PagingUtils<Media> toReturnMediaList = new PagingUtils<>(mediaList, pageNumber, pageSizeQuery, mediaCount);
            ResponseUtils.setPaginationLinks(res, toReturnMediaList, uriInfo);
            return res.build();
        } catch(MoovieListNotFoundException m){
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        } catch(UserNotLoggedException | InvalidAccessToResourceException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Only returns the 5 more relatedLists. They are related in
    @GET
    @Path("{id}/recommendedLists")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getRecommendedLists(@PathParam("id") final int id) {
        try{
            List<MoovieListDto> mlcList = MoovieListDto.fromMoovieListList(moovieListService.getRecommendedMoovieListCards(id, 4,0), uriInfo);
            javax.ws.rs.core.Response.ResponseBuilder res = javax.ws.rs.core.Response.ok(new GenericEntity<List<MoovieListDto>>(mlcList) {
            });
            return res.build();
        } catch(MoovieListNotFoundException m){
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        } catch(UserNotLoggedException | InvalidAccessToResourceException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Only returns the 5 more relatedLists. They are related in
    @GET
    @Path("{id}/recommendedMedia")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getRecommendedMediaToAdd(@PathParam("id") final int id) {
        try{
            List<MediaDto> mlcList = MediaDto.fromMediaList(moovieListService.getRecommendedMediaToAdd(id, 5), uriInfo);
            javax.ws.rs.core.Response.ResponseBuilder res = javax.ws.rs.core.Response.ok(new GenericEntity<List<MediaDto>>(mlcList) {
            });
            return res.build();
        } catch(MoovieListNotFoundException m){
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
        } catch(UserNotLoggedException | InvalidAccessToResourceException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* MOOVIELISTREVIEWS */
    @GET
    @Path("/{id}/moovieListReviews")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response getMoovieListReviewsFromListId(@PathParam("id") final int listId, @QueryParam("pageNumber") @DefaultValue("1") final int page) {
        try {
            final List<MoovieListReview> moovieListReviews = reviewService.getMoovieListReviewsByMoovieListId(listId, PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(), page-1);
            final int moovieListReviewsCount = reviewService.getMoovieListReviewByMoovieListIdCount(listId);
            final List<MoovieListReviewDto> moovieListReviewDtos = MoovieListReviewDto.fromMoovieListReviewList(moovieListReviews, uriInfo);
            javax.ws.rs.core.Response.ResponseBuilder res =  javax.ws.rs.core.Response.ok(new GenericEntity<List<MoovieListReviewDto>>(moovieListReviewDtos) {});
            final PagingUtils<MoovieListReview> toReturnMoovieListReviews = new PagingUtils<>(moovieListReviews,page - 1,PagingSizes.REVIEW_DEFAULT_PAGE_SIZE.getSize(),moovieListReviewsCount);
            ResponseUtils.setPaginationLinks(res,toReturnMoovieListReviews,uriInfo);
            return res.build();
        } catch (RuntimeException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * POST METHODS
     */

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response createMoovieList(@Valid final MoovieListCreateDto listDto) {
        try {
            int listId = moovieListService.createMoovieList(
                    listDto.getName(),
                    MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType(),
                    listDto.getDescription()
            ).getMoovieListId();
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(String.valueOf(listId));

            return javax.ws.rs.core.Response.created(uriBuilder.build())
                    .entity("{\"message\":\"Movie list created successfully.\", \"url\": \"" + uriBuilder.build().toString() + "\"}")
                    .build();


        } catch (DuplicateKeyException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.CONFLICT)
                    .entity("A movie list with the same name already exists.")
                    .build();

        } catch (RuntimeException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/{moovieListId}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response insertMediaIntoMoovieList(@PathParam("moovieListId") int moovieListId,
                                                               @Valid MediaListDto mediaIdListDto) {
        List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
        MoovieList updatedList = moovieListService.insertMediaIntoMoovieList(moovieListId, mediaIdList);
        if (mediaIdList == null || mediaIdList.isEmpty()) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity(new ResponseMessage("No media IDs provided."))
                    .build();
        }
        return javax.ws.rs.core.Response.ok(updatedList).entity(new ResponseMessage("Media added successfully to the list.")).build();

    }






    @POST
    @Path("{id}/moovieListReview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response createMoovieListReview(@PathParam("id") int listId, @Valid final MoovieListReviewCreateDto moovieListReviewDto) {
        reviewService.createReview(
                listId,
                0,
                moovieListReviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MOOVIE_LIST
        );

        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.CREATED)
                .entity("MoovieList review successfully created to the list with ID: " + listId)
                .build();
    }

    /**
     * PUT METHODS
     */

    @PUT
    @Path("{id}/moovieListReview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response editReview(@PathParam("id") int listId, @Valid final MoovieListReviewCreateDto moovieListReviewDto) {
        reviewService.editReview(
                listId,
                0,
                moovieListReviewDto.getReviewContent(),
                ReviewTypes.REVIEW_MOOVIE_LIST
        );

        return javax.ws.rs.core.Response.ok()
                .entity("MoovieList review successfully updated for media with ID: " + listId)
                .build();
    }

    /**
     * DELETE METHODS
     */

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response deleteMoovieList(@PathParam("id") final int id) {
        moovieListService.deleteMoovieList(id);
        return javax.ws.rs.core.Response.noContent().build();
    }


    @DELETE
    @Path("/{id}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response deleteMediaMoovieList(@PathParam("id") final int id, @Valid MediaListDto mediaIdListDto) {
        List<Integer> mediaIdList = mediaIdListDto.getMediaIdList();
        for (int media: mediaIdList){
            moovieListService.deleteMediaFromMoovieList(id, media);
        }
        return javax.ws.rs.core.Response.noContent().build();
    }


/*
    @POST
    @Path("/{id}/content")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage addMediaToMoovieList(@PathParam("moovieListId") final int listId, @PathParam("mediaId") final int mediaId) {
        try {

            moovieListService.insertMediaIntoMoovieList(listId, mediaId);

            return ResponseMessage.status(ResponseMessage.Status.CREATED)
                    .entity("Media successfully added to the list with ID: " + listId)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseMessage.status(ResponseMessage.Status.BAD_REQUEST)
                    .entity("Invalid media or list ID: " + e.getMessage())
                    .build();
        } catch (RuntimeException e) {
            return ResponseMessage.status(ResponseMessage.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while adding media to the list: " + e.getMessage())
                    .build();
        }
    }
*/
}
