package academy.mindswap.resource;

import academy.mindswap.model.Order;
import academy.mindswap.service.OrderService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users/{userId}/orders")
public class OrderResource {

    @Inject
    OrderService orderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Order> list(@PathParam("userId") Long userId) {
        return orderService.listAll(userId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Order create(@PathParam("userId") Long userId, Order order) {
        return orderService.create(userId, order);
    }

    @PUT
    @Path("/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Order update(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, Order order) {
        return orderService.update(userId, orderId, order);
    }

    @DELETE
    @Path("/{orderId}")
    @Transactional
    public void delete(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {
        orderService.delete(userId, orderId);
    }

}
