package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.User.User;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("users")
@Component
public class UserController {

    private static int DEFAULT_PAGE_INT = 1;

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(@QueryParam("page") @DefaultValue("1") final int page) {
        if (page<DEFAULT_PAGE_INT)
            return Response.status(Response.Status.BAD_REQUEST).build();

        final List<User> all = userService.listAll(page);

        if(all.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        List<UserDto> dtoList = UserDto.fromUserList(all, uriInfo);
        return Response.ok(new GenericEntity<List<UserDto>>(dtoList) {}).build();
    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") final int id){
        final User user = userService.findUserById(id);
        if(user == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UserDto.fromUser(user, uriInfo)).build();
    }

    public void create(){

    }

    public void update(){

    }

    public void delete(){

    }

}