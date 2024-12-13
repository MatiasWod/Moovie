package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UnableToBanUserException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UnableToBanUserEM implements ExceptionMapper<UnableToBanUserException> {
    @Override
    public Response toResponse(UnableToBanUserException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(e.getMessage())
                .build();
    }
}
