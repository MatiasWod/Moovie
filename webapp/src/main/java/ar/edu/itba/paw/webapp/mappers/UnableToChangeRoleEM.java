package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.exceptions.UnableToChangeRoleException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UnableToChangeRoleEM implements ExceptionMapper<UnableToChangeRoleException> {
    @Override
    public Response toResponse(UnableToChangeRoleException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
    }
}
