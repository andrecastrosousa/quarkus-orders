package academy.mindswap.resource;

import academy.mindswap.dto.OrderCreateDto;
import academy.mindswap.dto.OrderDto;
import academy.mindswap.service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/orders")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<OrderDto> list(@Context SecurityContext securityContext) {
        System.out.println(securityContext.getUserPrincipal().getName());
        return orderService.listAll(securityContext.getUserPrincipal().getName());
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public OrderDto get(@PathParam("orderId") Long orderId, @Context SecurityContext securityContext) {
        return orderService.findById(securityContext.getUserPrincipal().getName(), orderId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto create(@Context SecurityContext securityContext, OrderCreateDto order) {
        return orderService.create(securityContext.getUserPrincipal().getName(), order);
    }

    @DELETE
    @Path("/{orderId}")
    @RolesAllowed("user")
    @Transactional
    public void delete(@PathParam("orderId") Long orderId, @Context SecurityContext securityContext) {
        orderService.delete(securityContext.getUserPrincipal().getName(), orderId);
    }
}
