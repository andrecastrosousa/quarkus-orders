package academy.mindswap.resource;

import academy.mindswap.dto.ItemCreateDto;
import academy.mindswap.dto.ItemDto;
import academy.mindswap.model.Item;
import academy.mindswap.service.ItemService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/items")
public class ItemResource {

    @Inject
    ItemService itemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemDto> get() {
        return itemService.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ItemDto get(@PathParam("id") Long itemId) {
        return itemService.findById(itemId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ItemDto post(ItemCreateDto itemCreateDto) {
        return itemService.create(itemCreateDto);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ItemDto put(@PathParam("id") Long itemId, ItemDto itemDto) {
        return itemService.update(itemId, itemDto);
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public void delete(@PathParam("id") Long itemId) {
        itemService.delete(itemId);
    }
}
