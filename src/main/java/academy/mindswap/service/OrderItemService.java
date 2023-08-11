package academy.mindswap.service;

import academy.mindswap.dto.OrderDto;
import academy.mindswap.dto.OrderItemDto;
import academy.mindswap.dto.OrderItemUpdateDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getListOfOrderItem(Long userId, Long orderId);

    OrderDto addItemToOrder(Long userId, Long orderId, OrderItemDto orderItemAddDto);

    OrderDto updateItemOnOrder(Long userId, Long orderId, Long itemId, OrderItemUpdateDto OrderItemUpdateDto);

    void removeItemFromOrder(Long userId, Long orderId, Long itemId);
}
