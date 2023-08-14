package academy.mindswap.service;

import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getListOfOrderItem(String email, Long orderId);

    OrderDto addItemToOrder(String email, Long orderId, OrderItemDto orderItemAddDto);

    OrderDto updateItemOnOrder(String email, Long orderId, Long itemId, OrderItemUpdateDto OrderItemUpdateDto);

    void removeItemFromOrder(String email, Long orderId, Long itemId);
}
