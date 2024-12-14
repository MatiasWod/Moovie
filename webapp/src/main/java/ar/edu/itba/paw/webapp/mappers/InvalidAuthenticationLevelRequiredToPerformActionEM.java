package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.InvalidAuthenticationLevelRequiredToPerformActionException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidAuthenticationLevelRequiredToPerformActionEM implements ExceptionMapper<InvalidAuthenticationLevelRequiredToPerformActionException> {
    @Override
    public Response toResponse(InvalidAuthenticationLevelRequiredToPerformActionException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    }
}
