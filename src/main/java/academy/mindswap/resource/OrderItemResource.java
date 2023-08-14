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
        return orderItemService.getListOfOrderItem(securityContext.getUserPrincipal().getName(), orderId);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto addItemToOrder(
            @PathParam("orderId") Long orderId,
            @Context SecurityContext securityContext,
            OrderItemDto orderItemAddDto
    ) {
        return orderItemService.addItemToOrder(securityContext.getUserPrincipal().getName(), orderId, orderItemAddDto);
    }

    @PUT
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Transactional
    public OrderDto updateItemOnOrder(
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId,
            @Context SecurityContext securityContext,
            OrderItemUpdateDto orderItemUpdateDto
    ) {
        return orderItemService.updateItemOnOrder(securityContext.getUserPrincipal().getName(), orderId, itemId, orderItemUpdateDto);
    }

    @DELETE
    @Path("/{itemId}")
    @RolesAllowed("user")
    public void removeItemFromOrder(
            @PathParam("orderId") Long orderId,
            @PathParam("itemId") Long itemId,
            @Context SecurityContext securityContext
    ) {
        orderItemService.removeItemFromOrder(securityContext.getUserPrincipal().getName(), orderId, itemId);
    }
}
