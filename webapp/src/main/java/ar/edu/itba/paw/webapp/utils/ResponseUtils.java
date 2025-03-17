package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.PagingUtils;

import javax.ws.rs.core.*;

public class ResponseUtils {

    public ResponseUtils() {
        throw new AssertionError();
    }

    private final static int MAX_AGE=31536000;

    public static <T> void setPaginationLinks(Response.ResponseBuilder res, PagingUtils<T> pagingUtils, UriInfo uriInfo){
        if(pagingUtils.hasPreviousPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getCurrentPage() - 1).build().toString(),"previous");
        }
        if(pagingUtils.hasNextPage()){
            res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getCurrentPage() + 1).build().toString(),"next");
        }
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getFirstPage()).build().toString(),"first");
        res.link(uriInfo.getRequestUriBuilder().replaceQueryParam("page",pagingUtils.getLastPage()).build().toString(),"last");
        res.header("Total-Elements",pagingUtils.getTotalCount());
    }


    public static void setMaxAgeCache(Response.ResponseBuilder responseBuilder) {
        setConditionalCache(responseBuilder, MAX_AGE);
    }

    public static void setConditionalCache(Response.ResponseBuilder responseBuilder, int maxAge) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAge);
        responseBuilder.cacheControl(cacheControl);
    }
}
