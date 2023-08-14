package academy.mindswap.resource;

import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;
import academy.mindswap.service.OrderItemService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/orders/{orderId}/items")
public class OrderItemResource {

    @Inject
    OrderItemService orderItemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<OrderItemDto> get(@PathParam("orderId") Long orderId, @Context SecurityContext securityContext) {
        System.out.println(securityContext.getUserPrincipal().getName());
        return orderItemService.getListOfOrderItem(1L, orderId);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto addItemToOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, OrderItemDto orderItemAddDto) {
        return orderItemService.addItemToOrder(userId, orderId, orderItemAddDto);
    }

    @PUT
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto updateItemOnOrder(
            @PathParam("userId") Long userId,
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId,
            OrderItemUpdateDto orderItemUpdateDto
    ) {
        return orderItemService.updateItemOnOrder(userId, orderId, itemId, orderItemUpdateDto);
    }

    @DELETE
    @Path("/{itemId}")
    @RolesAllowed("user")
    public void removeItemFromOrder(
            @PathParam("userId") Long userId,
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId
    ) {
        orderItemService.removeItemFromOrder(userId, orderId, itemId);
    }
}
