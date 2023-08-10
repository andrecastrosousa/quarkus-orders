package academy.mindswap.resource;

import academy.mindswap.dto.OrderItemAddDto;
import academy.mindswap.model.Item;
import academy.mindswap.model.Order;
import academy.mindswap.model.OrderItem;
import academy.mindswap.service.OrderItemService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/users/{userId}/orders/{orderId}/items")
public class OrderItemResource {

    @Inject
    OrderItemService orderItemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderItem> get(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {
        return orderItemService.getListOfOrderItem(userId, orderId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Order addItemToOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, OrderItemAddDto orderItemAddDto) {
        try {
            return orderItemService.addItemToOrder(userId, orderId, orderItemAddDto);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
