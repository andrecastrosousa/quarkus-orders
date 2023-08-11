package academy.mindswap.resource;

import academy.mindswap.dto.UserCreateDto;
import academy.mindswap.dto.UserDto;
import academy.mindswap.model.User;
import academy.mindswap.service.UserService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users")
public class UserResource {


    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> get() {
        return userService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public UserDto post(@Valid UserCreateDto userCreateDto) {
        return userService.create(userCreateDto);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/bulk")
    @Transactional
    public List<UserDto> post(List<UserCreateDto> users) {
        return userService.create(users);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        userService.delete(id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") Long id) {
        return userService.getById(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User get(@PathParam("id") Long id, User user) {
        return userService.update(id, user);
    }


}
