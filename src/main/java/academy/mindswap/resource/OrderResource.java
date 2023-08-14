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
        return orderService.listAll(1L);
    }

    @GET
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public OrderDto get(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {
        return orderService.findById(userId, orderId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto create(@PathParam("userId") Long userId, OrderCreateDto order) {
        return orderService.create(userId, order);
    }

    @DELETE
    @Path("/{orderId}")
    @RolesAllowed("user")
    @Transactional
    public void delete(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {
        orderService.delete(userId, orderId);
    }

    /*@PUT
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Order update(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, Order order) {
        return orderService.update(userId, orderId, order);
    }*/
}
