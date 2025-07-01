package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.PagingUtils;

import javax.ws.rs.core.*;
import java.util.function.Supplier;

public class ResponseUtils {

    public ResponseUtils() {
        throw new AssertionError();
    }

    private final static int MAX_AGE=31536000;

    public static <T> void setPaginationLinks(Response.ResponseBuilder res, PagingUtils<T> pagingUtils, UriInfo uriInfo){
        if(pagingUtils.hasPreviousPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("pageNumber",pagingUtils.getCurrentPage() - 1).build().toString(),"previous");
        }
        if(pagingUtils.hasNextPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("pageNumber",pagingUtils.getCurrentPage() + 1).build().toString(),"next");
        }
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("pageNumber",pagingUtils.getFirstPage()).build().toString(),"first");
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("pageNumber",pagingUtils.getLastPage()).build().toString(),"last");
        res.header("Total-Elements",pagingUtils.getTotalCount());
    }


    public static void setMaxAgeCache(Response.ResponseBuilder responseBuilder) {
        setConditionalCache(responseBuilder,MAX_AGE);
    }


    public static void setConditionalCache(Response.ResponseBuilder responseBuilder,int maxAge) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAge);
        responseBuilder.cacheControl(cacheControl);
    }

    public static <T> Response setConditionalCacheHash(Request request, Supplier<T> dto, int hashCode) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);

        final EntityTag eTag = new EntityTag(String.valueOf(hashCode));
        Response.ResponseBuilder response = request.evaluatePreconditions(eTag);

        if (response == null) {
            response = Response.ok(dto.get()).tag(eTag);
            cacheControl.setNoStore(false);
        }

        return response.header(HttpHeaders.VARY, "Accept, Content-Type").cacheControl(cacheControl).build();
    }

}
